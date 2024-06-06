package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.sdk.alipay;

import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;

public class AlipaySignaturer implements Signaturer {
    @Override
    public String encrypt(String encryptFunc, String data) {
        return "";
    }

    @Override
    public String decrypt(String encryptFunc, String encryptedData) {
        return "";
    }

    @Override
    public String sign(String signFunc, String data) {
        return "";
    }

    @Override
    public boolean verifySignature(String verifySignFunc, String data, String signature) {
        return false;
    }
}
