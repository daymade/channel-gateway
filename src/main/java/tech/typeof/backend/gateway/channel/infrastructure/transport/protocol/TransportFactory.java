package tech.typeof.backend.gateway.channel.infrastructure.transport.protocol;

import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http.HttpTransport;

@Component
public class TransportFactory {
    public static Transport getTransport(String type) {
        switch (type.toLowerCase()) {
            case "http":
                return new HttpTransport();
            default:
                throw new IllegalArgumentException("Unknown formatter type: " + type);
        }
    }
}
