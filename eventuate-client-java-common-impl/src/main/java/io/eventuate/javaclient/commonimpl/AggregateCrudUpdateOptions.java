package io.eventuate.javaclient.commonimpl;

import io.eventuate.EventContext;
import io.eventuate.javaclient.commonimpl.encryption.EncryptionKey;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Optional;

public class AggregateCrudUpdateOptions {

  private final Optional<EventContext> triggeringEvent;
  private final Optional<SerializedSnapshot> snapshot;
  private final Optional<EncryptionKey> encryptionKey;

  public AggregateCrudUpdateOptions() {
    triggeringEvent = Optional.empty();
    snapshot = Optional.empty();
    encryptionKey = Optional.empty();
  }

  public AggregateCrudUpdateOptions(Optional<EventContext> triggeringEvent, Optional<SerializedSnapshot> snapshot) {
    this(triggeringEvent, snapshot, Optional.empty());
  }

  public AggregateCrudUpdateOptions(Optional<EventContext> triggeringEvent, Optional<SerializedSnapshot> snapshot, Optional<EncryptionKey> encryptionKey) {
    this.triggeringEvent = triggeringEvent;
    this.snapshot = snapshot;
    this.encryptionKey = encryptionKey;
  }

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public Optional<SerializedSnapshot> getSnapshot() {
    return snapshot;
  }

  public Optional<EncryptionKey> getEncryptionKey() {
    return encryptionKey;
  }

  public AggregateCrudUpdateOptions withSnapshot(SerializedSnapshot serializedSnapshot) {
    return new AggregateCrudUpdateOptions(this.triggeringEvent, Optional.of(serializedSnapshot));
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public AggregateCrudUpdateOptions withTriggeringEvent(EventContext eventContext) {
    return new AggregateCrudUpdateOptions(Optional.of(eventContext), snapshot);
  }

}
