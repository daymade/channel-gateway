package tech.typeof.backend.gateway.channel.infrastructure.codec.format;

public interface DataFormatter {
    String format(Object data);
    <T> T parse(String data, Class<T> clazz);
}
