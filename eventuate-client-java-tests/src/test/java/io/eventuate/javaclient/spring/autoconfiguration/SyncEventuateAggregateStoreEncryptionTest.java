package io.eventuate.javaclient.spring.autoconfiguration;

import io.eventuate.*;
import io.eventuate.encryption.EventEncryptor;
import io.eventuate.javaclient.saasclient.EventuateAggregateStoreBuilder;
import io.eventuate.sync.EventuateAggregateStore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AutoConfigurationIntegrationTestConfiguration.class)
@IntegrationTest
public class SyncEventuateAggregateStoreEncryptionTest extends AbstractEventuateAggregateStoreEncryptionTest {

  private EventuateAggregateStore aggregateStore = EventuateAggregateStoreBuilder.standard().buildSync();

  @Override
  protected EntityIdAndVersion save(String data) {
    return aggregateStore.save(SomeAggregate.class,
            Collections.singletonList(new SomeEvent(data)),
            new SaveOptions().withEncryptionKeyId(keyId));
  }

  @Override
  protected EntityIdAndVersion update(EntityIdAndVersion entityIdAndVersion, String data) {
    return aggregateStore.update(SomeAggregate.class,
            entityIdAndVersion,
            Collections.singletonList(new SomeEvent(data)),
            new UpdateOptions().withEncryptionKeyId(keyId));
  }

  @Override
  protected EntityWithMetadata<SomeAggregate> find(EntityIdAndVersion entityIdAndVersion) {
    return aggregateStore.find(SomeAggregate.class,
            entityIdAndVersion.getEntityId());
  }

  @Override
  protected void setEventEncryptor(Optional<EventEncryptor> eventEncryptor) {
    aggregateStore.setEventEncryptor(eventEncryptor);
  }
}
