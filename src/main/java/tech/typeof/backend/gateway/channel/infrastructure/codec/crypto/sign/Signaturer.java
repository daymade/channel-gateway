package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign;

public interface Signaturer {
    String encrypt(String encryptFunc, String data);
    String decrypt(String encryptFunc, String encryptedData);
    String sign(String signFunc, String data);
    boolean verifySignature(String verifySignFunc, String data, String signature);
}
