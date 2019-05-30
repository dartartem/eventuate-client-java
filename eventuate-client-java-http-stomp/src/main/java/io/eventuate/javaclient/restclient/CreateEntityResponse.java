package io.eventuate.javaclient.restclient;

import io.eventuate.common.id.generator.Int128;

import java.util.List;

public class CreateEntityResponse {


  private String entityId;
  private Int128 entityVersion;
  private List<Int128> eventIds;

  public CreateEntityResponse() {
  }

  public CreateEntityResponse(String entityId, Int128 entityVersion, List<Int128> eventIds) {
    this.entityId = entityId;
    this.entityVersion = entityVersion;
    this.eventIds = eventIds;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public Int128 getEntityVersion() {
    return entityVersion;
  }

  public void setEntityVersion(Int128 entityVersion) {
    this.entityVersion = entityVersion;
  }

  public List<Int128> getEventIds() {
    return eventIds;
  }

  public void setEventIds(List<Int128> eventIds) {
    this.eventIds = eventIds;
  }
}
