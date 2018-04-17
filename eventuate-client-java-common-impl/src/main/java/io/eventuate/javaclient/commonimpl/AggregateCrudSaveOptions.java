package io.eventuate.javaclient.commonimpl;

import io.eventuate.EventContext;
import io.eventuate.javaclient.commonimpl.encryption.EncryptionKey;

import java.util.Optional;

public class AggregateCrudSaveOptions {

  private final Optional<String> entityId;
  private final Optional<EventContext> triggeringEvent;
  private final Optional<EncryptionKey> encryptionKey;

  public AggregateCrudSaveOptions() {
    entityId = Optional.empty();
    triggeringEvent = Optional.empty();
    encryptionKey = Optional.empty();
  }

  public AggregateCrudSaveOptions(Optional<EventContext> triggeringEvent, Optional<String> entityId) {
    this(triggeringEvent, entityId, Optional.empty());
  }

  public AggregateCrudSaveOptions(Optional<EventContext> triggeringEvent, Optional<String> entityId, Optional<EncryptionKey> encryptionKey) {
    this.triggeringEvent = triggeringEvent;
    this.entityId = entityId;
    this.encryptionKey = encryptionKey;
  }

  public Optional<String> getEntityId() {
    return entityId;
  }

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public Optional<EncryptionKey> getEncryptionKey() {
    return encryptionKey;
  }

  public AggregateCrudSaveOptions withEventContext(EventContext eventContext) {
    return new AggregateCrudSaveOptions(Optional.of(eventContext), entityId, encryptionKey);
  }

  public AggregateCrudSaveOptions withId(String entityId) {
    return new AggregateCrudSaveOptions(triggeringEvent, Optional.of(entityId), encryptionKey);
  }
}
