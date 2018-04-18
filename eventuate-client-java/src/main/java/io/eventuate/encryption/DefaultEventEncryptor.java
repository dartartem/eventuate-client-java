package io.eventuate.encryption;


import org.springframework.security.crypto.encrypt.Encryptors;

public class DefaultEventEncryptor implements EventEncryptor {
  private EventEncryptorKey eventEncryptorKey;

  public DefaultEventEncryptor(EventEncryptorKey eventEncryptorKey) {
    this.eventEncryptorKey = eventEncryptorKey;
  }

  @Override
  public String encrypt(String keyId, String data) {
    EventEncryptionKey eventEncryptionKey = eventEncryptorKey.getEncryptionKey(keyId);

    return Encryptors.text(eventEncryptionKey.getKey(), eventEncryptionKey.getSalt()).encrypt(data);
  }

  @Override
  public String decrypt(String keyId, String data) {
    EventEncryptionKey eventEncryptionKey = eventEncryptorKey.getEncryptionKey(keyId);

    return Encryptors.text(eventEncryptionKey.getKey(), eventEncryptionKey.getSalt()).decrypt(data);
  }
}
