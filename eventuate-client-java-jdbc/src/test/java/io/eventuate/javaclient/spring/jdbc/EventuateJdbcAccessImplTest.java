package io.eventuate.javaclient.spring.jdbc;

import io.eventuate.EntityIdAndType;
import io.eventuate.javaclient.commonimpl.*;
import io.eventuate.javaclient.commonimpl.encryption.EncryptedEventData;
import io.eventuate.javaclient.commonimpl.encryption.EncryptionKey;
import io.eventuate.javaclient.commonimpl.encryption.EncryptionKeyStore;
import io.eventuate.javaclient.commonimpl.encryption.EventDataEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public abstract class EventuateJdbcAccessImplTest {

  public static class Config {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
      return new JdbcTemplate(dataSource);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EventuateJdbcAccess eventuateJdbcAccess(JdbcTemplate jdbcTemplate, EventuateSchema eventuateSchema) {
      return new EventuateJdbcAccessImpl(jdbcTemplate, eventuateSchema);
    }
  }

  private static final String testAggregate = "testAggregate1";
  private static final String testEventType = "testEventType1";
  private static final String testEventData = "testEventData1";

  private static final EncryptionKey testEncryptionKey1 = new EncryptionKey("1", "testPass1", "481e155a423f11e8842f0ed5f89f718b");
  private static final EncryptionKey testEncryptionKey2 = new EncryptionKey("2", "testPass2", "481e1a0a423f11e8842f0ed5f89f718b");

  private static final EncryptionKeyStore testKeyStore = new EncryptionKeyStore() {
    private Map<String, EncryptionKey> idKeyMap = new HashMap<>();

    {
      idKeyMap.put("1", testEncryptionKey1);
      idKeyMap.put("2", testEncryptionKey2);
    }

    @Override
    public EncryptionKey findEncryptionKeyById(String keyId) {
      return idKeyMap.get(keyId);
    }
  };

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private EventuateSchema eventuateSchema;

  @Autowired
  private EventuateJdbcAccess eventuateJdbcAccess;

  protected abstract String readAllEventsSql();

  protected abstract String readAllEntitiesSql();

  protected abstract String readAllSnapshots();

  @Test
  public void testSave() {
    EventTypeAndData eventTypeAndData = new EventTypeAndData(testEventType, testEventData, Optional.empty());

    eventuateJdbcAccess.save(testAggregate, Collections.singletonList(eventTypeAndData), Optional.empty());

    List<Map<String, Object>> events = jdbcTemplate.queryForList(readAllEventsSql());
    Assert.assertEquals(1, events.size());

    List<Map<String, Object>> entities = jdbcTemplate.queryForList(readAllEntitiesSql());
    Assert.assertEquals(1, entities.size());
  }

  @Test
  public void testFind() {
    EventTypeAndData eventTypeAndData = new EventTypeAndData(testEventType, testEventData, Optional.empty());

    SaveUpdateResult saveUpdateResult = eventuateJdbcAccess.save(testAggregate, Collections.singletonList(eventTypeAndData), Optional.empty());

    LoadedEvents loadedEvents = eventuateJdbcAccess.find(testAggregate, saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(), Optional.empty());

    Assert.assertEquals(1, loadedEvents.getEvents().size());
  }

  @Test
  public void testUpdate() {
    EventTypeAndData eventTypeAndData = new EventTypeAndData(testEventType, testEventData, Optional.empty());
    SaveUpdateResult saveUpdateResult = eventuateJdbcAccess.save(testAggregate, Collections.singletonList(eventTypeAndData), Optional.empty());


    EntityIdAndType entityIdAndType = new EntityIdAndType(saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(), testAggregate);
    eventTypeAndData = new EventTypeAndData("testEventType2", "testEventData2", Optional.empty());

    eventuateJdbcAccess.update(entityIdAndType,
            saveUpdateResult.getEntityIdVersionAndEventIds().getEntityVersion(),
            Collections.singletonList(eventTypeAndData), Optional.of(new AggregateCrudUpdateOptions(Optional.empty(), Optional.of(new SerializedSnapshot("", "")))));

    List<Map<String, Object>> events = jdbcTemplate.queryForList(readAllEventsSql());
    Assert.assertEquals(2, events.size());

    List<Map<String, Object>> entities = jdbcTemplate.queryForList(readAllEntitiesSql());
    Assert.assertEquals(1, entities.size());

    List<Map<String, Object>> snapshots = jdbcTemplate.queryForList(readAllSnapshots());
    Assert.assertEquals(1, snapshots.size());

    LoadedEvents loadedEvents = eventuateJdbcAccess.find(testAggregate, saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(), Optional.empty());
    Assert.assertTrue(loadedEvents.getSnapshot().isPresent());
  }

  @Test
  public void testSaveFindUpdateEncrypted() {
    EventTypeAndData eventTypeAndData = new EventTypeAndData(testEventType, testEventData, Optional.empty());

    SaveUpdateResult saveUpdateResult = eventuateJdbcAccess.save(testAggregate,
            Collections.singletonList(eventTypeAndData),
            Optional.of(new AggregateCrudSaveOptions(Optional.empty(), Optional.empty(), Optional.of(testEncryptionKey1))));

    LoadedEvents loadedEvents = eventuateJdbcAccess.find(testAggregate,
            saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(),
            Optional.of(new AggregateCrudFindOptions(Optional.empty(), Optional.of(testKeyStore))));

    Assert.assertEquals(1, loadedEvents.getEvents().size());
    Assert.assertEquals(testEventData, loadedEvents.getEvents().get(0).getEventData());

    List<Map<String, Object>> dbData = readEventsDirectlyFromDb(saveUpdateResult.getPublishableEvents().getAggregateType(),
            saveUpdateResult.getPublishableEvents().getEntityId());

    Assert.assertEquals(1, dbData.size());

    EncryptedEventData encryptedEventData = EncryptedEventData.fromEventDataString((String)dbData.get(0).get("event_data"));

    String hexRegExp = "^[0-9a-fA-F]+$";

    Assert.assertFalse(testEventData.matches(hexRegExp));
    Assert.assertTrue(encryptedEventData.getData().matches(hexRegExp));

    String newEventData = testEventData + "Updated";

    saveUpdateResult = eventuateJdbcAccess.update(new EntityIdAndType(saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(), saveUpdateResult.getPublishableEvents().getAggregateType()),
            saveUpdateResult.getEntityIdVersionAndEventIds().getEntityVersion(),
            Collections.singletonList(new EventTypeAndData(testEventType, newEventData, Optional.empty())),
            Optional.of(new AggregateCrudUpdateOptions(Optional.empty(), Optional.empty(), Optional.of(testEncryptionKey1))));

    loadedEvents = eventuateJdbcAccess.find(testAggregate,
            saveUpdateResult.getEntityIdVersionAndEventIds().getEntityId(),
            Optional.of(new AggregateCrudFindOptions(Optional.empty(), Optional.of(testKeyStore))));

    Assert.assertEquals(2, loadedEvents.getEvents().size());
    Assert.assertEquals(newEventData, loadedEvents.getEvents().get(1).getEventData());

    dbData = readEventsDirectlyFromDb(saveUpdateResult.getPublishableEvents().getAggregateType(),
            saveUpdateResult.getPublishableEvents().getEntityId());

    Assert.assertEquals(2, dbData.size());

    encryptedEventData = EncryptedEventData.fromEventDataString((String)dbData.get(1).get("event_data"));

    Assert.assertFalse(newEventData.matches(hexRegExp));
    Assert.assertTrue(encryptedEventData.getData().matches(hexRegExp));
  }

  private List<Map<String, Object>> readEventsDirectlyFromDb(String aggregateType, String entityId) {
    return jdbcTemplate.queryForList(String.format("select event_data from %s where entity_type = ? and entity_id = ? order by event_id asc", eventuateSchema.qualifyTable("events")),
            aggregateType, entityId);
  }

  protected List<String> loadSqlScriptAsListOfLines(String script) throws IOException {
    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/eventuate-embedded-schema.sql")))) {
      return bufferedReader.lines().collect(Collectors.toList());
    }
  }

  protected void executeSql(List<String> sqlList) {
    jdbcTemplate.execute(sqlList.stream().collect(Collectors.joining("\n")));
  }
}
