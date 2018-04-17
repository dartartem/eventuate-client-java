package io.eventuate.encryption;

public class EncryptionKey {
  private String id;
  private String key;
  private String salt;

  public EncryptionKey(String id, String key, String salt) {
    this.id = id;
    this.key = key;
    this.salt = salt;
  }

  public String getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }
}
