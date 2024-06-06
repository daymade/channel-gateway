package tech.typeof.backend.gateway.channel.adapter.api.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GatewayRefundRequest {
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
     * 支付网关订单流水号
     */
    private String outTradeNo;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 交易描述, 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加
     */
    private String productDescription;

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
