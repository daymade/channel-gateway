package tech.typeof.backend.gateway.channel.domain.adapter;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayRefundRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayRefundResponse;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.model.ParamsExtractor;
import tech.typeof.backend.gateway.channel.domain.model.ParamsValidator;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.util.HashMap;

@Data
@Slf4j
@Accessors(chain = true)
public class ChannelAdapter {
    private TemplateRenderer protoRender;
    private ParamsExtractor paramsExtractor;
    private ParamsValidator paramsValidator;
    private Transport transport;
    private Signaturer signaturer;
    private DataFormatter dataFormatter;
    private Channel channel;

    /**
     * 构造报文
     *
     * @param config     渠道协议配置
     * @param signedData 加密后的报文
     * @return 发给渠道接口的完整报文
     */
    private static HashMap<String, Object> buildPayload(Channel.HTTPConfig config, String signedData) {
        var paymentConfig = new HashMap<String, Object>();
        paymentConfig.put("scheme", config.getScheme());
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
     * 找到对应渠道发起支付请求
     *
     * @param request 支付请求参数
     * @return 是否成功
     */
    public GatewayPaymentResponse<String> pay(GatewayPaymentRequest request) {
        paramsValidator.validate(request);

        // 拿到 HTTP 协议配置，包括报文模板和加密等配置
        var config = channel.getTransportConfig("Payment");
        log.info("transport config: {}, for request: {}", config, request);

        // 提取参数
        var params = paramsExtractor.extractParams(request, config.getMappings());
        var body = protoRender.renderTemplate(config.getTemplate(), params);
        var signedData = signaturer.sign(config.getSignFunc(), body);

        // 构造报文
        var payload = buildPayload(config, signedData);

        try {
            log.info("about to send payload: {}", payload);

            var paymentResponse = transport.sendRequest(null, payload);
            return GatewayPaymentResponse.success(paymentResponse);
        } catch (RuntimeException e) {
            log.error("Error sending payment request", e);

            // 不要向客户端透出服务器异常信息
            return GatewayPaymentResponse.failure("Failed to send payment request, please try again later");
        }
    }

    public GatewayRefundResponse<String> refund(GatewayRefundRequest request) {
        paramsValidator.validate(request);

        // 拿到 HTTP 协议配置，包括报文模板和加密等配置
        var config = channel.getTransportConfig("Refund");

        log.info("transport config: {}, for request: {}", config, request);

        // 提取参数
        var params = paramsExtractor.extractParams(request, config.getMappings());
        var body = protoRender.renderTemplate(config.getTemplate(), params);
        var signedData = signaturer.sign(config.getSignFunc(), body);

        // 构造报文
        var payload = buildPayload(config, signedData);

        return GatewayRefundResponse.success("Refund success");
    }
}
