package io.eventuate.encryption;

public class NoEncryptionKeyProvidedException extends RuntimeException {
  private EncryptedEventData encryptedEventData;

  public NoEncryptionKeyProvidedException(EncryptedEventData encryptedEventData) {
    this.encryptedEventData = encryptedEventData;
  }

  public EncryptedEventData getEncryptedEventData() {
    return encryptedEventData;
  }
}
