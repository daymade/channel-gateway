package groovy


import java.security.PrivateKey
import java.security.Signature
import java.security.SignatureException

String doSign(String content, PrivateKey privateKey, String charset) {
    try {
        Signature signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(content.getBytes(charset))

        byte[] signed = signature.sign()
        return Base64.getEncoder().encodeToString(signed)
    } catch (Exception e) {
        throw new SignatureException("RSA签名[content = ${content}; charset = ${charset}]发生异常!", e)
    }
}