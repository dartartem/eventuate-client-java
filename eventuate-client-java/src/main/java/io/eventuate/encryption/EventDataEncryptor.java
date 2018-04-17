package io.eventuate.encryption;

import org.springframework.security.crypto.encrypt.Encryptors;

public class EventDataEncryptor {
  public String encrypt(EncryptionKey encryptionKey, String eventData) {
    return Encryptors.text(encryptionKey.getKey(), encryptionKey.getSalt()).encrypt(eventData);
  }

  public String decrypt(EncryptionKey encryptionKey, String eventData) {
    return Encryptors.text(encryptionKey.getKey(), encryptionKey.getSalt()).decrypt(eventData);
  }
}
