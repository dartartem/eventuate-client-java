package io.eventuate;

import io.eventuate.encryption.EncryptionKey;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Optional;

public class FindOptions {

  private Optional<EventContext> triggeringEvent = Optional.empty();
  private Optional<EncryptionKey> encryptionKey = Optional.empty();

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public Optional<EncryptionKey> getEncryptionKey() {
    return encryptionKey;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public FindOptions withTriggeringEvent(EventContext eventContext) {
    this.triggeringEvent = Optional.ofNullable(eventContext);
    return this;
  }

  public FindOptions withTriggeringEvent(Optional<EventContext> eventContext) {
    this.triggeringEvent = eventContext;
    return this;
  }

  public FindOptions withEncryptionKey(EncryptionKey encryptionKey) {
    this.encryptionKey = Optional.of(encryptionKey);
    return this;
  }
}
