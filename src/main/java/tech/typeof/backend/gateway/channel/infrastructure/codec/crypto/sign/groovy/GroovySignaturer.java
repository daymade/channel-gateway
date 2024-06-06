package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class GroovySignaturer implements Signaturer {

    private static final String GROOVY_DO_SIGN_METHOD = "doSign";

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

    public String doSign(String data, String scriptCode, String privateKeyPEM) throws Exception {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);

        Script script = shell.parse(scriptCode);

        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPEM);

        // 调用Groovy脚本中的doSign方法
        var encoding = "UTF-8";

        String result = (String) script.invokeMethod(GROOVY_DO_SIGN_METHOD,
                new Object[]{data, privateKey, encoding});

        return result;
    }

    private PrivateKey getPrivateKey(String key) throws GeneralSecurityException {
        // Remove the first and last lines
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        return keyFactory.generatePrivate(keySpec);
    }
}
