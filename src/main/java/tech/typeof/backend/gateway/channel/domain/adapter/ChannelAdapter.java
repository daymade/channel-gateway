package tech.typeof.backend.gateway.channel.domain.adapter;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.constants.ProtocolConstants;
import tech.typeof.backend.gateway.channel.domain.model.ParamsExtractor;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.util.HashMap;
import java.util.Optional;

@Data
@Slf4j
@Accessors(chain = true)
public class ChannelAdapter {
    private TemplateRenderer protoRender;
    private ParamsExtractor paramsExtractor;
    private Transport transport;
    private Signaturer signaturer;
    private DataFormatter dataFormatter;
    private Channel channel;

    /**
     * 找到对应渠道发起支付请求
     *
     * @param request 支付请求参数
     * @return 是否成功
     */
    public GatewayPaymentResponse<String> pay(GatewayPaymentRequest request) {
        // 检查请求是否合法
        var error = checkError(request);
        if (error != null) {
            return GatewayPaymentResponse.failure("Bad request：" + error);
        }

        // 拿到 HTTP 协议配置，包括报文模板和加密等配置
        var config = getTransportConfig(request);

        // 提取参数
        var params = paramsExtractor.extractParams(request);
        var body = protoRender.renderTemplate(config.getTemplate(), params);
        var signedData = signaturer.sign(config.getSignFunc(), body);

        // 构造报文
        var payload = buildPayload(config, signedData);

        try {
            var paymentResponse = transport.sendRequest(payload);
            return GatewayPaymentResponse.success(paymentResponse);
        } catch (RuntimeException e) {
            log.error("Error sending payment request", e);
            return GatewayPaymentResponse.failure("Failed to send payment request: " + e.getMessage());
        }
    }

    /**
     * 拿到 HTTP 协议配置
     *
     * @return HTTP 协议配置，包括报文模板和加密等配置
     */
    private Channel.HTTPConfig getTransportConfig(GatewayPaymentRequest request) {
        // TODO 根据 request 找到 pay 支付能力再找到对应接口，目前 PoC 随便找一个接口试一下
        var ability = channel.getAbilities().getFirst();

        // 获取支付能力和协议配置
        var protocol = Optional.of(ability)
                .map(a -> a.getProtocol())
                .orElseThrow(() -> new ChannelGatewayException("No protocol found for ability " + ability));

        // PoC 只支持 HTTP 协议
        if (!ProtocolConstants.HTTP.equalsIgnoreCase(protocol)) {
            throw new ChannelGatewayException("Unsupported protocol: " + protocol);
        }

        return Optional.of(ability)
                .map(a -> a.getProtocolConfig())
                .map(pc -> pc.getHttpConfig())
                .orElseThrow(() -> new ChannelGatewayException("No protocol config found for ability " + ability));
    }

    /**
     * 构造报文
     *
     * @param config     渠道协议配置
     * @param signedData 加密后的报文
     * @return 发给渠道接口的完整报文
     */
    private static HashMap<String, Object> buildPayload(Channel.HTTPConfig config, String signedData) {
        var paymentConfig = new HashMap<String, Object>();
        paymentConfig.put("host", config.getHost());
        paymentConfig.put("port", config.getPort());
        paymentConfig.put("url", config.getUrl());
        paymentConfig.put("verb", config.getVerb());
        paymentConfig.put("headers", config.getHeaders());
        paymentConfig.put("queryParams", config.getQueryParams());
        paymentConfig.put("body", signedData);
        return paymentConfig;
    }

    /**
     * 检查请求是否合法
     *
     * @param request 支付请求
     * @return 具体错误规则，合法时为 null
     */
    private String checkError(GatewayPaymentRequest request) {
        // TODO 改成通用的 Validator
        return null;
    }
}
