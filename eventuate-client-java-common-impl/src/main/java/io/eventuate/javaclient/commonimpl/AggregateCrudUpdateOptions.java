package io.eventuate.javaclient.commonimpl;

import io.eventuate.EventContext;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Optional;

public class AggregateCrudUpdateOptions {

  private final Optional<EventContext> triggeringEvent;
  private final Optional<SerializedSnapshot> snapshot;

  public AggregateCrudUpdateOptions() {
    triggeringEvent = Optional.empty();
    snapshot = Optional.empty();
  }

  public AggregateCrudUpdateOptions(Optional<EventContext> triggeringEvent, Optional<SerializedSnapshot> snapshot) {
    this.triggeringEvent = triggeringEvent;
    this.snapshot = snapshot;
  }

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public Optional<SerializedSnapshot> getSnapshot() {
    return snapshot;
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
