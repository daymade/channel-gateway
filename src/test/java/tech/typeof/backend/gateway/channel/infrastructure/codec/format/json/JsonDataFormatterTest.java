
package tech.typeof.backend.gateway.channel.infrastructure.codec.format.json;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JsonDataFormatterTest {

    private JsonDataFormatter jsonDataFormatter = new JsonDataFormatter();

    @Test
    void testFormat_withValidData() {
        Map<String, String> data = new HashMap<>();
        data.put("key", "value");

        String expected = "{\"key\":\"value\"}";
        String actual = jsonDataFormatter.format(data);
        assertEquals(expected, actual);
    }

    @Test
    void testFormat_withInvalidData() {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        ReflectionTestUtils.setField(jsonDataFormatter, "objectMapper", objectMapperMock);

        RuntimeException runtimeException = new RuntimeException("Error formatting JSON");

        try {
            when(objectMapperMock.writeValueAsString(Mockito.any())).thenThrow(new JsonProcessingException("Test") {});
        } catch (JsonProcessingException e) {
            //ignore
        }

        assertThrows(RuntimeException.class, () -> jsonDataFormatter.format(new Object[]{}), "Error formatting JSON");
    }
}
