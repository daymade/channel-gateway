package tech.typeof.backend.gateway.channel.infrastructure.transport.protocol;

import java.util.Map;

public interface Transport {
    String sendRequest(Map<String, Object> config) throws RuntimeException;
}
