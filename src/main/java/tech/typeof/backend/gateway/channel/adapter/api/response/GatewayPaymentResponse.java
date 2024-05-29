package tech.typeof.backend.gateway.channel.adapter.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GatewayPaymentResponse<T> {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";

    /**
     * 防止外部调用, 只能通过 {@link tech.typeof.backend.gateway.channel.adapter.api.utils.ResponseUtils} 调用
     */
    private GatewayPaymentResponse() {
    }

    /**
     * 请求处理状态, 只有3个合法值 ["SUCCESS", "FAILED", "UNKNOWN"]
     */
    private String status;

    /**
     * 错误信息, 当 status = FAILED 时包含详细错误
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public static <T> GatewayPaymentResponse<T> success(T data) {
        return new GatewayPaymentResponse<T>()
                .setStatus(SUCCESS)
                .setData(data);
    }

    public static <T> GatewayPaymentResponse<T> failure(String message) {
        return failure(message, null);
    }

    public static <T> GatewayPaymentResponse<T> failure(String message, T data) {
        return new GatewayPaymentResponse<T>()
                .setStatus(FAILED)
                .setMessage(message)
                .setData(data);
    }
}