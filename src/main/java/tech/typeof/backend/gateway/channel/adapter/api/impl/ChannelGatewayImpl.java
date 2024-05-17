package tech.typeof.backend.gateway.channel.adapter.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.ChannelGateway;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;
import tech.typeof.backend.gateway.channel.app.payment.PayService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ChannelGatewayImpl implements ChannelGateway {
    private final PayService payService;

    @Override
    public GatewayPaymentResponse pay(GatewayPaymentRequest request) throws ChannelGatewayException {
        return payService.pay(request);
    }

    @Override
    public GatewayPaymentResponse refund(String transactionId, BigDecimal amount) throws ChannelGatewayException {
        return null;
    }

    @Override
    public GatewayPaymentResponse cancel(String transactionId) throws ChannelGatewayException {
        return null;
    }
}
