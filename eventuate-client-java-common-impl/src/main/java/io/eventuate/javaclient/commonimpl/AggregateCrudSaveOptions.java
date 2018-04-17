package io.eventuate.javaclient.commonimpl;

import io.eventuate.EventContext;

import java.util.Optional;

public class AggregateCrudSaveOptions {

  private final Optional<String> entityId;
  private final Optional<EventContext> triggeringEvent;

  public AggregateCrudSaveOptions() {
    entityId = Optional.empty();
    triggeringEvent = Optional.empty();
  }

  public AggregateCrudSaveOptions(Optional<EventContext> triggeringEvent, Optional<String> entityId) {
    this.triggeringEvent = triggeringEvent;
    this.entityId = entityId;
  }

  public Optional<String> getEntityId() {
    return entityId;
  }

  public Optional<EventContext> getTriggeringEvent() {
    return triggeringEvent;
  }

  public AggregateCrudSaveOptions withEventContext(EventContext eventContext) {
    return new AggregateCrudSaveOptions(Optional.of(eventContext), entityId);
  }

  public AggregateCrudSaveOptions withId(String entityId) {
    return new AggregateCrudSaveOptions(triggeringEvent, Optional.of(entityId));
  }
}
