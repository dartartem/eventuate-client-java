package io.eventuate.javaclient.spring.autoconfiguration;

import io.eventuate.*;
import io.eventuate.encryption.EventEncryptor;
import io.eventuate.encryption.NoEventEncryptorProvidedException;
import io.eventuate.javaclient.saasclient.EventuateAggregateStoreBuilder;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AutoConfigurationIntegrationTestConfiguration.class)
@IntegrationTest
public class EventuateAggregateStoreEncryptionTest extends AbstractEventuateAggregateStoreEncryptionTest {

  private io.eventuate.EventuateAggregateStore aggregateStore = EventuateAggregateStoreBuilder.defaultFromEnv();

  @Override
  protected EntityIdAndVersion save(String data) throws Exception {
    return aggregateStore.save(SomeAggregate.class,
            Collections.singletonList(new SomeEvent(data)),
            new SaveOptions().withEncryptionKeyId(keyId)).get();
  }

  @Override
  protected EntityIdAndVersion update(EntityIdAndVersion entityIdAndVersion, String data) throws Exception {
    return aggregateStore.update(SomeAggregate.class,
            entityIdAndVersion,
            Collections.singletonList(new SomeEvent(data)),
            new UpdateOptions().withEncryptionKeyId(keyId)).get();
  }

  @Override
  protected EntityWithMetadata<SomeAggregate> find(EntityIdAndVersion entityIdAndVersion) throws Exception {
    try {
      return aggregateStore.find(SomeAggregate.class,
              entityIdAndVersion.getEntityId()).get();
    } catch (ExecutionException e) {
      Assert.assertTrue(e.getCause() instanceof NoEventEncryptorProvidedException);
      throw (NoEventEncryptorProvidedException) e.getCause();
    }
  }

  @Override
  protected void setEventEncryptor(Optional<EventEncryptor> eventEncryptor) {
    aggregateStore.setEventEncryptor(eventEncryptor);
  }
}
