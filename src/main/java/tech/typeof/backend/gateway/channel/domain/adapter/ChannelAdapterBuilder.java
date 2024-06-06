package tech.typeof.backend.gateway.channel.domain.adapter;

import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.model.ParamsExtractor;
import tech.typeof.backend.gateway.channel.domain.model.ParamsValidator;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.js.JsSignaturer;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.json.JsonDataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http.HttpTransport;

import java.util.Objects;
import java.util.Optional;

import static tech.typeof.backend.gateway.channel.domain.constants.ProtocolConstants.HTTP;
import static tech.typeof.backend.gateway.channel.domain.constants.ProtocolConstants.JSON;

@Component
public class ChannelAdapterBuilder {

    public ChannelAdapter findAdapter(Channel channel) {
        var channelAdapter = new ChannelAdapter()
                .setChannel(channel);

        // TODO PoC 就抓第一个测试一下
        var ability = channel.getAbilities().getFirst();

        if (!HTTP.equalsIgnoreCase(ability.getProtocol())) {
            throw new ChannelGatewayException(String.format("%s is not a HTTP channel", ability.getProtocol()));
        }

        channelAdapter.setTransport(new HttpTransport());

        var bodyFormat = Optional.of(ability)
                .map(a -> a.getProtocolConfig())
                .map(pc -> pc.getHttpConfig())
                .map(hc -> hc.getBodyFormat())
                .orElseThrow(() -> new ChannelGatewayException("cannot find body format for " + ability));

        if (!JSON.equalsIgnoreCase(bodyFormat)) {
            throw new ChannelGatewayException("cannot find data formatter of %s for%s".formatted(bodyFormat, ability));
        }
        channelAdapter.setDataFormatter(new JsonDataFormatter());

        channelAdapter.setParamsValidator(new ParamsValidator());
        channelAdapter.setProtoRender(new TemplateRenderer());
        channelAdapter.setParamsExtractor(new ParamsExtractor());
        channelAdapter.setSignaturer(new JsSignaturer());

        // 返回之前验证一下 adapter 是否可用
        validate(channelAdapter);

        return channelAdapter;
    }

    /**
     * 验证所有必需的属性是否已正确设置
     */
    protected void validate(ChannelAdapter channelAdapter) {
        Objects.requireNonNull(channelAdapter.getProtoRender(), "TemplateRenderer must be set");
        Objects.requireNonNull(channelAdapter.getParamsExtractor(), "ParamsExtractor must be set");
        Objects.requireNonNull(channelAdapter.getTransport(), "Transport must be set");
        Objects.requireNonNull(channelAdapter.getSignaturer(), "Signaturer must be set");
        Objects.requireNonNull(channelAdapter.getDataFormatter(), "DataFormatter must be set");
        Objects.requireNonNull(channelAdapter.getChannel(), "Channel must be set");
    }
}
