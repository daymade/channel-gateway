package tech.typeof.backend.gateway.channel.domain.channel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.domain.channel.thirdparty.alipay.SdkConfig;
import tech.typeof.backend.gateway.channel.domain.constants.ProtocolConstants;
import tech.typeof.backend.gateway.channel.infrastructure.codec.mapping.MappingRule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class Channel {
    private String name;

    private Boolean isEnabled;

    // 用于签名的私钥, 生产环境要找个 keycenter 存储，禁止写在代码里
    private String privateKeyPEM;

    private List<PaymentAbility> abilities;

    /**
     * 根据 request 找到对应接口, 例如 pay 支付能力拿到渠道配置的支付接口 HTTP 协议配置
     *
     * @param abilityName 能力名称
     * @return HTTP 协议配置，包括报文模板和加密等配置
     */
    public HTTPConfig getTransportConfig(String abilityName) {
        var ability = getAbilities().stream()
                .filter(a -> a.getName().equals(abilityName))
                .findFirst()
                .orElseThrow(() -> new ChannelGatewayException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No ability found for " + abilityName));

        // 获取支付能力和协议配置
        var protocol = Optional.of(ability)
                .map(a -> a.getProtocol())
                .orElseThrow(() -> new ChannelGatewayException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No protocol found for ability " + abilityName));

        // PoC 只支持 HTTP 协议
        if (!ProtocolConstants.HTTP.equalsIgnoreCase(protocol)) {
            throw new ChannelGatewayException(HttpStatus.INTERNAL_SERVER_ERROR, "Unsupported protocol: " + protocol);
        }

        return Optional.of(ability)
                .map(a -> a.getProtocolConfig())
                .map(pc -> pc.getHttpConfig())
                .orElseThrow(() -> new ChannelGatewayException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No protocol config found for ability " + abilityName));
    }

    @Data
    public static class PaymentAbility {
        private String name;
        private String protocol;
        private ProtocolConfig protocolConfig;
    }

    @Data
    public static class ProtocolConfig {
        private HTTPConfig httpConfig;
        private SdkConfig sdkConfig;
        // Add other protocol configurations here, e.g., TCPConfig
    }

    @Data
    public static class HTTPConfig {
        private String scheme;
        private String host;
        private int port;
        private String url;
        private String verb;
        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private List<MappingRule> mappings;
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
