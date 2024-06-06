package tech.typeof.backend.gateway.channel.domain.model;

import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.infrastructure.codec.mapping.MappingRule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParamsExtractorTest {

    @Test
    void testExtractParams() {
        // 创建一个 ParamsExtractor 实例
        ParamsExtractor extractor = new ParamsExtractor();

        // 创建一个 request 对象
        var request = new GatewayPaymentRequest();
        request.setAmount(BigDecimal.valueOf(9.00));

        // 创建一个 MappingRule 列表
        List<MappingRule> mappings = new ArrayList<>();
        MappingRule rule = new MappingRule();
        rule.setSource("amount");
        rule.setTarget("total_amount");
        rule.setRequired(true);
        mappings.add(rule);

        // 调用 extractParams 方法
        Map<String, Object> result = extractor.extractParams(request, mappings);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("total_amount"));
        assertEquals(BigDecimal.valueOf(9.00), result.get("total_amount"));
    }
}