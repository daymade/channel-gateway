package tech.typeof.backend.gateway.channel.infrastructure.transport.protocol;

import tech.typeof.backend.gateway.channel.domain.core.GatewayContext;

import java.util.Map;

public interface Transport {
    String sendRequest(GatewayContext context, Map<String, Object> config) throws RuntimeException;
}
