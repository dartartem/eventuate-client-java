package io.eventuate.javaclient.commonimpl.encryption;

public interface EncryptionKeyStore {
  EncryptionKey findEncryptionKeyById(String keyId);
}
