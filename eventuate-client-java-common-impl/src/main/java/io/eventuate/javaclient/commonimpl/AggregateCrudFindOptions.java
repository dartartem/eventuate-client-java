package io.eventuate.javaclient.commonimpl;

import io.eventuate.EventContext;
import io.eventuate.javaclient.commonimpl.encryption.EncryptionKeyStore;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Optional;

public class AggregateCrudFindOptions {

  private Optional<EventContext> triggeringEvent;
  private Optional<EncryptionKeyStore> encryptionKeyStore;

  public AggregateCrudFindOptions() {
    triggeringEvent = Optional.empty();
    encryptionKeyStore = Optional.empty();
  }

  public AggregateCrudFindOptions(Optional<EventContext> triggeringEvent) {
    this(triggeringEvent, Optional.empty());
  }

  public AggregateCrudFindOptions(Optional<EventContext> triggeringEvent, Optional<EncryptionKeyStore> encryptionKeyStore) {
    this.triggeringEvent = triggeringEvent;
    this.encryptionKeyStore = encryptionKeyStore;
  }

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public Optional<EncryptionKeyStore> getEncryptionKeyStore() {
    return encryptionKeyStore;
  }

  public AggregateCrudFindOptions withTriggeringEvent(EventContext eventContext) {
    this.triggeringEvent = Optional.ofNullable(eventContext);
    return this;
  }

  public AggregateCrudFindOptions withTriggeringEvent(Optional<EventContext> eventContext) {
    this.triggeringEvent = eventContext;
    return this;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}
