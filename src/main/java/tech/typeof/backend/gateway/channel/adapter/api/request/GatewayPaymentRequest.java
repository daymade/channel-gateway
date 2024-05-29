package tech.typeof.backend.gateway.channel.adapter.api.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GatewayPaymentRequest {
    /**
     * 业务身份
     */
    private String bizCode;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 币种，例如 USD, CNY
     */
    private String currency;

    /**
     * 支付方式，例如 CREDIT_CARD, PAYPAL
     */
    private String paymentMethod;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 回调URL
     */
    private String callbackUrl;

    /**
     * 顾客邮箱
     */
    private String customerEmail;

    /**
     * 顾客电话
     */
    private String customerPhone;
}