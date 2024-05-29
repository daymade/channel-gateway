package tech.typeof.backend.gateway.channel.adapter.api.exception;

public class ChannelGatewayException extends RuntimeException {
    public ChannelGatewayException(String message) {
        super(message);
    }

    public ChannelGatewayException(String message, RuntimeException cause) {
        super(message, cause);
    }
}
