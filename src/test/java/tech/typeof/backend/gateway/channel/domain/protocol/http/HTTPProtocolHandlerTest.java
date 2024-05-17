package tech.typeof.backend.gateway.channel.domain.protocol.http;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http.HttpTransport;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class HTTPProtocolHandlerTest {

    @Test
    public void testSendRequest() throws Exception {
        // Create a mock HttpClient
        HttpClient httpClientMock = mock(HttpClient.class);

        // Create test configuration
        Map<String, Object> testConfig = new HashMap<>();
        testConfig.put("host", "localhost");
        testConfig.put("port", 8080);
        testConfig.put("url", "/test");
        testConfig.put("verb", "GET");

        // Create a mock response
        HttpResponse<String> responseMock = mock(HttpResponse.class);
        when(responseMock.body()).thenReturn("test response");

        // Make the mock client return the mock response when called
        when(httpClientMock.send(any(), any())).thenAnswer(new Answer<>() {
            @Override
            public HttpResponse<String> answer(InvocationOnMock invocation) {
                return responseMock;
            }
        });

        // Create an instance of the class to test, with the mock client injected
        HttpTransport httpProtocolHandler = new HttpTransport(httpClientMock);

        // run sendRequest method
        String response = httpProtocolHandler.sendRequest(testConfig);

        // Check that the response matches the mock response body
        assertEquals("test response", response);
    }

    @Test
    public void testSendRequestPay() throws Exception {
        // Create a mock HttpClient
        HttpClient httpClientMock = mock(HttpClient.class);

        // Create a mock response
        HttpResponse<String> responseMock = mock(HttpResponse.class);
        when(responseMock.body()).thenReturn("test response");

        // Make the mock client return the mock response when called
        when(httpClientMock.send(any(), any())).thenAnswer(new Answer<>() {
            @Override
            public HttpResponse<String> answer(InvocationOnMock invocation) {
                return responseMock;
            }
        });

        // Create an instance of the class to test, with the mock client injected
        HttpTransport httpProtocolHandler = new HttpTransport(httpClientMock);

        // 支付接口配置
        Map<String, Object> paymentConfig = new HashMap<>();
        paymentConfig.put("host", "api.paymentprovider.com");
        paymentConfig.put("port", 80);
        paymentConfig.put("url", "/pay");
        paymentConfig.put("verb", "POST");
        paymentConfig.put("headers", Map.of("Content-Type", "application/json"));
        paymentConfig.put("queryParams", Map.of("token", "abcdef"));
        paymentConfig.put("body", "{\"amount\": 100.50, \"currency\": \"USD\"}");

        String paymentResponse = httpProtocolHandler.sendRequest(paymentConfig);
        System.out.println("支付接口响应: " + paymentResponse);
    }
}