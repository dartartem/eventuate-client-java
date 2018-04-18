package io.eventuate.javaclient.spring.autoconfiguration;

import io.eventuate.*;
import io.eventuate.encryption.*;
import io.vertx.core.json.Json;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public abstract class AbstractEventuateAggregateStoreEncryptionTest {

  private static String hexRegExp = "^[0-9a-fA-F]+$";

  protected static final String keyId = "1";
  private static final String key = "Super strong password";
  private static final String salt = "47b4033642e911e8842f0ed5f89f718b";

  private static final String testEventData = "Some secret data";
  private static final String testEventDataUpdated = "Some secret data (Updated)";

  protected static final EventEncryptionKey eventEncryptionKey = new EventEncryptionKey(keyId, key, salt);

  protected EventEncryptor eventEncryptor = new DefaultEventEncryptor(keyId -> {
    if (AbstractEventuateAggregateStoreEncryptionTest.keyId.equals(keyId)) {
      return eventEncryptionKey;
    }

    throw new IllegalArgumentException("No encryption key found!");
  });

  @Before
  public void initEventEncryptor() {
    setEventEncryptor(Optional.of(eventEncryptor));
  }

  @Test
  public void testSaveEncrypted() throws Exception {
    EntityIdAndVersion entityIdAndVersion = save(testEventData);

    NoEventEncryptorProvidedException noEventEncryptorProvidedException = null;

    try {
      setEventEncryptor(Optional.empty());
      find(entityIdAndVersion);
    } catch (NoEventEncryptorProvidedException e) {
      noEventEncryptorProvidedException = e;
    }

    Assert.assertNotNull(noEventEncryptorProvidedException);

    String data = noEventEncryptorProvidedException.getEncryptedEventData().getData();

    Assert.assertNotNull(data);
    Assert.assertFalse(data.trim().isEmpty());
    Assert.assertTrue(data.matches(hexRegExp));
    Assert.assertEquals(Json.encode(new SomeEvent(testEventData)), eventEncryptor.decrypt(keyId, data));

    Assert.assertFalse(testEventData.matches(hexRegExp));
  }

  @Test
  public void testSaveAndFind() throws Exception {
    EntityIdAndVersion entityIdAndVersion = save(testEventData);

    EntityWithMetadata<SomeAggregate> entityEntityWithMetadata = find(entityIdAndVersion);

    Assert.assertEquals(1, entityEntityWithMetadata.getEvents().size());
    Assert.assertEquals(testEventData, ((SomeEvent)entityEntityWithMetadata.getEvents().get(0).getEvent()).getData());
  }

  @Test
  public void testUpdateAndFind() throws Exception {
    EntityIdAndVersion entityIdAndVersion = save(testEventData);

    entityIdAndVersion = update(entityIdAndVersion, testEventDataUpdated);

    EntityWithMetadata<SomeAggregate> entityEntityWithMetadata = find(entityIdAndVersion);

    Assert.assertEquals(2, entityEntityWithMetadata.getEvents().size());
    Assert.assertEquals(testEventDataUpdated,
            ((SomeEvent)entityEntityWithMetadata.getEvents().get(1).getEvent()).getData());
  }

  protected abstract EntityIdAndVersion save(String data) throws Exception;
  protected abstract EntityIdAndVersion update(EntityIdAndVersion entityIdAndVersion, String data) throws Exception;
  protected abstract EntityWithMetadata<SomeAggregate> find(EntityIdAndVersion entityIdAndVersion) throws Exception;
  protected abstract void setEventEncryptor(Optional<EventEncryptor> eventEncryptor);

  public static class SomeAggregate implements Aggregate<SomeAggregate> {
    @Override
    public SomeAggregate applyEvent(Event event) {
      return this;
    }
  }

  public static class SomeEvent implements Event {
    private String data;

    public SomeEvent() {
    }

    public SomeEvent(String data) {
      this.data = data;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
  }
}
