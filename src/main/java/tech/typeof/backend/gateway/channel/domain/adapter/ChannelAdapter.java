package tech.typeof.backend.gateway.channel.domain.adapter;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.model.ParamsExtractor;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class ChannelAdapter {
    private TemplateRenderer protoRender;
    private ParamsExtractor paramsExtractor;
    private Transport transport;
    private Signaturer signaturer;
    private DataFormatter dataFormatter;
    private Channel channel;

    // add a validate method to ensure all this property are correctly set
    public void validate() {
        Objects.requireNonNull(protoRender, "TemplateRenderer must be set");
        Objects.requireNonNull(paramsExtractor, "ParamsExtractor must be set");
        Objects.requireNonNull(transport, "Transport must be set");
        Objects.requireNonNull(signaturer, "Signaturer must be set");
        Objects.requireNonNull(dataFormatter, "DataFormatter must be set");
        Objects.requireNonNull(channel, "Channel must be set");
    }

    public GatewayPaymentResponse pay(GatewayPaymentRequest request){
        // TODO 找到 pay 支付能力再找到对应接口，目前 PoC 随便找一个接口试一下
        var ability = channel.getAbilities().get(0);

        if (!ability.getProtocol().equalsIgnoreCase("HTTP")) {
            throw new UnsupportedOperationException("Unsupported protocol: " + ability.getProtocol());
        }

        var config = ability.getProtocolConfig().getHttpConfig();

        var params = paramsExtractor.extractParams(request);
        var body = protoRender.renderTemplate(config.getTemplate(), params);
        var signedData = signaturer.sign(config.getSignFunc(), body);

        // 支付接口配置
        Map<String, Object> paymentConfig = new HashMap<>();
        paymentConfig.put("host", config.getHost());
        paymentConfig.put("port", config.getPort());
        paymentConfig.put("url", config.getUrl());
        paymentConfig.put("verb", config.getVerb());
        paymentConfig.put("headers", config.getHeaders());
        paymentConfig.put("queryParams", config.getQueryParams());
        paymentConfig.put("body", signedData);
        // check sign

        String paymentResponse = transport.sendRequest(paymentConfig);

        return new GatewayPaymentResponse("ok", "", paymentResponse);
    }
}
