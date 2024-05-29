package tech.typeof.backend.gateway.channel.adapter.api;

import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;

import java.math.BigDecimal;

public interface ChannelGateway {
    /**
     * Perform payment transaction.
     *
     * @param request the transaction amount.
     * @return GatewayPaymentResponse the response object representing the transaction's outcome.
     * @throws ChannelGatewayException if any error occurs during processing the payment.
     */
    GatewayPaymentResponse<String> pay(GatewayPaymentRequest request) throws ChannelGatewayException;

    /**
     * Perform refund transaction.
     *
     * @param transactionId the id of the transaction to refund.
     * @param amount        the amount to refund.
     * @return GatewayPaymentResponse the response object representing the transaction's outcome.
     * @throws ChannelGatewayException if any error occurs during processing the refund.
     */
    GatewayPaymentResponse refund(String transactionId, BigDecimal amount) throws ChannelGatewayException;

    /**
     * Cancel a transaction.
     *
     * @param transactionId the id of the transaction to cancel.
     * @return GatewayPaymentResponse the response object representing the transaction's outcome.
     * @throws ChannelGatewayException if any error occurs during processing the cancellation.
     */
    GatewayPaymentResponse cancel(String transactionId) throws ChannelGatewayException;
}
