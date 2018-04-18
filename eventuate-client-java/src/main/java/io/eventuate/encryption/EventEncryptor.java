package io.eventuate.encryption;

public interface EventEncryptor {
  String encrypt(String keyId, String data);
  String decrypt(String keyId, String data);
}
