package tech.typeof.backend.gateway.channel.adapter.api.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GatewayPaymentRequest {
    private String bizCode;           // 业务身份
    private BigDecimal amount;        // 交易金额
    private String currency;          // 币种，例如 USD, CNY
    private String paymentMethod;     // 支付方式，例如 CREDIT_CARD, PAYPAL
    private String orderId;           // 订单ID
    private String description;       // 交易描述
    private String callbackUrl;       // 回调URL
    private String customerEmail;     // 顾客邮箱
    private String customerPhone;     // 顾客电话
}
