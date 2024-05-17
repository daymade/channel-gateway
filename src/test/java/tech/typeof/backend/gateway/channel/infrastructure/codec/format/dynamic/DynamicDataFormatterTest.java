package tech.typeof.backend.gateway.channel.infrastructure.codec.format.dynamic;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;

public class DynamicDataFormatterTest {

    @Test
    void testFormat() {
        // Given
        DynamicDataFormatter formatter = new DynamicDataFormatter();

        Map<String, Object> data = Map.of("key", "value");

        // When
        String result = formatter.format(ProxyObject.fromMap(data));
        
        // Then
        assertEquals("{\"key\":\"value\"}", result);
    }

    @Test
    void testParse() {
        // Given
        DynamicDataFormatter formatter = new DynamicDataFormatter();
        String data = "{\"key\":\"value\"}";

        // When
        Map result = formatter.parse(data, Map.class);
        
        // Then
        assertEquals(Map.of("key", "value"), result);
    }
}