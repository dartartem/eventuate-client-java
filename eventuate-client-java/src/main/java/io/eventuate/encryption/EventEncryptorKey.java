package io.eventuate.encryption;

public interface EventEncryptorKey {
  EventEncryptionKey getEncryptionKey(String keyId);
}
