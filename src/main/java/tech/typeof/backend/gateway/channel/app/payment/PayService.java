package tech.typeof.backend.gateway.channel.app.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayRefundRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayRefundResponse;
import tech.typeof.backend.gateway.channel.app.channel.ChannelRouter;
import tech.typeof.backend.gateway.channel.app.proxy.ChannelProxy;
import tech.typeof.backend.gateway.channel.domain.adapter.ChannelAdapterBuilder;

@Component
@RequiredArgsConstructor
public class PayService {
    // TODO not implemented
    private ChannelProxy channelProxy;

    private final ChannelRouter channelRouter;
    private final ChannelAdapterBuilder channelAdapterBuilder;

    public GatewayPaymentResponse<String> pay(GatewayPaymentRequest request) throws ChannelGatewayException{
        var channel = channelRouter.routeToChannel(request.getBizCode());

        var channelAdapter = channelAdapterBuilder.findAdapter(channel);

        return channelAdapter.pay(request);
    }

    public GatewayRefundResponse<String> refund(GatewayRefundRequest request) throws ChannelGatewayException {
        var channel = channelRouter.routeToChannel(request.getBizCode());

        var channelAdapter = channelAdapterBuilder.findAdapter(channel);

        return channelAdapter.refund(request);
    }
}
