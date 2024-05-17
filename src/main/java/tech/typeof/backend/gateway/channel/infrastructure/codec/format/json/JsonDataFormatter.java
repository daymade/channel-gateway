package tech.typeof.backend.gateway.channel.infrastructure.codec.format.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;

public class JsonDataFormatter implements DataFormatter {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String format(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error formatting JSON", e);
        }
    }

    @Override
    public <T> T parse(String data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}

