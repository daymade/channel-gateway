package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.groovy;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.js.JsSignaturerTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroovySignaturerTest {

    public static String readTestResource(String resourcePath) throws IOException {
        ClassLoader classLoader = JsSignaturerTest.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @SneakyThrows
    @Test
    void sign_alipay_v3() {
        var signaturer = new GroovySignaturer();

        // load file from resources
        String scriptCode = readTestResource("groovy/GroovyScriptSigner.groovy");

        var data = """
                {
                  "out_trade_no": "order4562222",
                  "total_amount": 100.00,
                  "subject": "subject23",
                  "body": "Payment for services",
                  "timeout_express": "90m"
                }""";

        var result = signaturer.doSign(data, scriptCode, "");

        assertEquals("data", result, "Encryption output did not match input");
    }

}