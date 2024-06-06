package tech.typeof.backend.gateway.channel.app.payment;

import com.alipay.v3.ApiClient;
import com.alipay.v3.Configuration;
import com.alipay.v3.api.AlipayTradeApi;
import com.alipay.v3.model.AlipayTradePayModel;
import com.alipay.v3.model.AlipayTradePayResponseModel;
import com.alipay.v3.util.GenericExecuteApi;
import com.alipay.v3.util.model.AlipayConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class PayServiceTest {
    private final String URL = "https://openapi.alipay.com/gateway.do";
    // TODO 这里是错误的APPID，无法正常使用
    private final String APPID = "2016092000555555";
    // TODO 这里是错误的公私钥，无法正常使用
    private final String PRIVATE_KEY = "G9w0BAQEFAAOCAQ=";
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzZz7";

    @Test
    @SneakyThrows
    void pay() {
        // 这是支付宝V3 SDK 的 [文档](https://opendocs.alipay.com/open-v3/065bsc)
        ApiClient apiClient = Configuration.getDefaultApiClient();
        //设置网关地址
        apiClient.setBasePath(URL);
        //设置alipayConfig参数（全局设置一次）
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置应用ID
        alipayConfig.setAppId(APPID);
        //设置应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        apiClient.setAlipayConfig(alipayConfig);

        //实例化客户端
        AlipayTradeApi api = new AlipayTradeApi(apiClient);

        //调用 alipay.trade.pay
        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel()
                .outTradeNo("20210817010101001")
                .totalAmount("0.01")
                .subject("测试商品")
                .scene("bar_code")
                .authCode("28763443825664394");

        //发起调用
        AlipayTradePayResponseModel response = api.pay(alipayTradePayModel);
        System.out.println(response);

        // 上面这个客户端调用也可以改成 execute 通用方法

        //实例化客户端
        GenericExecuteApi genericExecuteApi = new GenericExecuteApi(apiClient);
        //设置业务参数
        Map<String, Object> bizParams = new HashMap<>();
        //设置bizContent
        Map<String, Object> otherParams = new HashMap<>();
        otherParams.put("body", "对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。");
        otherParams.put("subject", "大乐透");
        otherParams.put("out_trade_no", "70501111111S001111119");
        otherParams.put("timeout_express", "90m");
        otherParams.put("total_amount", 9.00);
        bizParams.put("biz_content", otherParams);

        String method = "alipay.trade.wap.pay";
        String httpMethod = "POST";//pageExecute支持 GET 和 POST 方式
        String responseStr = genericExecuteApi.pageExecute(method, httpMethod, bizParams);
    }
}