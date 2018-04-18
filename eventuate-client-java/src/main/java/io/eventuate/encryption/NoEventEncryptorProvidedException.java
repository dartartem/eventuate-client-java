package io.eventuate.encryption;

public class NoEventEncryptorProvidedException extends RuntimeException {
  private EncryptedEventData encryptedEventData;

  public NoEventEncryptorProvidedException(EncryptedEventData encryptedEventData) {
    this.encryptedEventData = encryptedEventData;
  }

  public EncryptedEventData getEncryptedEventData() {
    return encryptedEventData;
  }
}
