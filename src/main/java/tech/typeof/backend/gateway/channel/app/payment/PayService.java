package tech.typeof.backend.gateway.channel.app.payment;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.app.channel.ChannelGateway;
import tech.typeof.backend.gateway.channel.app.proxy.ChannelProxy;
import tech.typeof.backend.gateway.channel.domain.adapter.ChannelAdapterBuilder;

@Component
@RequiredArgsConstructor
public class PayService {
    private ChannelProxy channelProxy; // not implemented

    private final ChannelGateway channelGateway;
    private final ChannelAdapterBuilder channelAdapterBuilder;

    @SneakyThrows
    public GatewayPaymentResponse pay(GatewayPaymentRequest request) throws ChannelGatewayException{
        var channel = channelGateway.routeToChannel(request.getBizCode());

        var channelAdapter = channelAdapterBuilder.findAdapter(channel);

        return channelAdapter.pay(request);
    }
}
