package io.eventuate.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EncryptedEventData {
  public static String ENCRYPTED_EVENT_DATA_STRING_PREFIX = "__ENCRYPTED__";

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private String encryptionKeyId;
  private String data;

  public EncryptedEventData() {
  }

  public EncryptedEventData(String encryptionKeyId, String data) {
    this.encryptionKeyId = encryptionKeyId;
    this.data = data;
  }

  public static boolean isEventDataStringEncrypted(String eventData) {
    return eventData.startsWith(ENCRYPTED_EVENT_DATA_STRING_PREFIX);
  }

  public static EncryptedEventData fromEventDataString(String eventData) {
    String json = eventData.substring(ENCRYPTED_EVENT_DATA_STRING_PREFIX.length());

    try {
      return objectMapper.readValue(json, EncryptedEventData.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getEncryptionKeyId() {
    return encryptionKeyId;
  }

  public void setEncryptionKeyId(String encryptionKeyId) {
    this.encryptionKeyId = encryptionKeyId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String asString() {
    try {
      return ENCRYPTED_EVENT_DATA_STRING_PREFIX + objectMapper.writeValueAsString(this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
