package tech.typeof.backend.gateway.channel.domain.channel;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Channel {
    private String name;

    private Boolean isEnabled;

    private List<PaymentAbility> abilities;

    @Data
    public static class PaymentAbility {
        private String name;
        private String protocol;
        private ProtocolConfig protocolConfig;
    }

    @Data
    public static class ProtocolConfig {
        private HTTPConfig httpConfig;
        // Add other protocol configurations here, e.g., TCPConfig
    }

    @Data
    public static class HTTPConfig {
        private String host;
        private int port;
        private String url;
        private String verb;
        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private String template;
        private String encryptFunc;
        private String signFunc;
        private String bodyFormat;
    }

    // Future protocol configurations can be defined here
    // @Getter
    // @Setter
    // @AllArgsConstructor
    // public static class TCPConfig {
    //     private String host;
    //     private int port;
    //     // Add other TCP-specific configuration parameters
    // }
}
