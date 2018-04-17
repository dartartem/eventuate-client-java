package io.eventuate.javaclient.commonimpl.encryption;

import io.eventuate.javaclient.commonimpl.JSonMapper;

public class EncryptedEventData {
  public static String ENCRYPTED_EVENT_DATA_STRING_PREFIX = "__ENCRYPTED__";

  private String encryptionKeyId;
  private String data;

  public EncryptedEventData() {
  }

  public EncryptedEventData(String encryptionKeyId, String data) {
    this.encryptionKeyId = encryptionKeyId;
    this.data = data;
  }

  public static boolean checkIfEventDataStringIsEncrypted(String eventData) {
    return eventData.startsWith(ENCRYPTED_EVENT_DATA_STRING_PREFIX);
  }

  public static EncryptedEventData fromEventDataString(String eventData) {
    String json = eventData.substring(ENCRYPTED_EVENT_DATA_STRING_PREFIX.length());
    return JSonMapper.fromJson(json, EncryptedEventData.class);
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
    return ENCRYPTED_EVENT_DATA_STRING_PREFIX + JSonMapper.toJson(this);
  }
}
