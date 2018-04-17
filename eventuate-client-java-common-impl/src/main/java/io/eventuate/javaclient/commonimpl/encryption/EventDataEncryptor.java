package io.eventuate.javaclient.commonimpl.encryption;

import org.springframework.security.crypto.encrypt.Encryptors;

public class EventDataEncryptor {
  public static String encrypt(EncryptionKey encryptionKey, String eventData) {
    return Encryptors.text(encryptionKey.getKey(), encryptionKey.getSalt()).encrypt(eventData);
  }

  public static String decrypt(EncryptionKey encryptionKey, String eventData) {
    return Encryptors.text(encryptionKey.getKey(), encryptionKey.getSalt()).decrypt(eventData);
  }
}
