package tech.typeof.backend.gateway.channel.infrastructure.codec.format;

import tech.typeof.backend.gateway.channel.infrastructure.codec.format.json.JsonDataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.xml.XmlDataFormatter;

public class DataFormatterFactory {
    public static DataFormatter getFormatter(String type) {
        switch (type.toLowerCase()) {
            case "json":
                return new JsonDataFormatter();
            case "xml":
                return new XmlDataFormatter();
            default:
                throw new IllegalArgumentException("Unknown formatter type: " + type);
        }
    }
}
