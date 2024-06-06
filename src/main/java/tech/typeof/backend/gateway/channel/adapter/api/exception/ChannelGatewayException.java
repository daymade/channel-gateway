package tech.typeof.backend.gateway.channel.adapter.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ChannelGatewayException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ChannelGatewayException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ChannelGatewayException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ChannelGatewayException(HttpStatus httpStatus, String message, Exception cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}