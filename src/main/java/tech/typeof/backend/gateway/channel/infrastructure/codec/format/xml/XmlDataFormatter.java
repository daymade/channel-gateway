package tech.typeof.backend.gateway.channel.infrastructure.codec.format.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;

public class XmlDataFormatter implements DataFormatter {
    @Override
    public String format(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T parse(String data, Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet.");

    }
}

