package tech.typeof.backend.gateway.channel.adapter.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GatewayRefundResponse<T> {
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";
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

    /**
     * 防止外部调用生成不合法响应
     */
    private GatewayRefundResponse() {
    }

    public static <T> GatewayRefundResponse<T> success(T data) {
        return new GatewayRefundResponse<T>()
                .setStatus(SUCCESS)
                .setData(data);
    }

    public static <T> GatewayRefundResponse<T> failure(String message) {
        return failure(message, null);
    }

    public static <T> GatewayRefundResponse<T> failure(String message, T data) {
        return new GatewayRefundResponse<T>()
                .setStatus(FAILED)
                .setMessage(message)
                .setData(data);
    }

    // 请求是否成功
    public boolean isSuccessful() {
        return SUCCESS.equals(status);
    }
}
