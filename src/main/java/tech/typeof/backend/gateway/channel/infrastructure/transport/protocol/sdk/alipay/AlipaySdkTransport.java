package tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.sdk.alipay;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.util.GenericExecuteApi;
import com.alipay.v3.util.model.AlipayConfig;
import org.jetbrains.annotations.NotNull;
import tech.typeof.backend.gateway.channel.domain.core.GatewayContext;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.util.HashMap;
import java.util.Map;

public class AlipaySdkTransport implements Transport {
    // 支付宝 bizParams 的字段名
    private static final String BIZ_CONTENT_KEY = "biz_content";

    private final String URL = "https://openapi.alipay.com/gateway.do";
    // TODO 这里是错误的APPID，无法正常使用
    private final String APPID = "2016092000555555";
    // TODO 这里是错误的公私钥，无法正常使用
    private final String PRIVATE_KEY = "G9w0BAQEFAAOCAQ=";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzZz7";

    @Override
    public String sendRequest(GatewayContext context, Map<String, Object> config) throws RuntimeException {
        ApiClient apiClient = null;
        try {
            apiClient = getApiClient();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        //实例化客户端
        var genericExecuteApi = new GenericExecuteApi(apiClient);

        //设置业务参数
        Map<String, Object> bizParams = new HashMap<>();
        //设置bizContent
        bizParams.put(BIZ_CONTENT_KEY, context.getMap(BIZ_CONTENT_KEY));

        // config.get("bizContent");
        // config.get("method");
        // config.get("httpMethod");
        var method = "alipay.trade.wap.pay";
        //pageExecute支持 GET 和 POST 方式
        var httpMethod = "POST";

        String responseStr = null;
        try {
            responseStr = genericExecuteApi.pageExecute(method, httpMethod, bizParams);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        return responseStr;
    }

    private @NotNull ApiClient getApiClient() throws ApiException {
        // 这是支付宝V3 SDK 的 [文档](https://opendocs.alipay.com/open-v3/065bsc)
        var apiClient = Configuration.getDefaultApiClient();
        //设置网关地址
        apiClient.setBasePath(URL);
        //设置alipayConfig参数（全局设置一次）
        var alipayConfig = new AlipayConfig();
        //设置应用ID
        alipayConfig.setAppId(APPID);
        //设置应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        apiClient.setAlipayConfig(alipayConfig);
        return apiClient;
    }
}
