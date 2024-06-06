package tech.typeof.backend.gateway.channel.domain.engine.template;

import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtoRenderTest {

    @Test
    void renderTemplate() {
        var render = new TemplateRenderer();
        var map = new HashMap<String, Object>();
        map.put("currency", "USD");
        map.put("amount", 100);

        var json = render.renderTemplate("""
                {
                    "currency": "[[${currency}]]",
                    "amount": [[${amount}]]
                }""", map);

        System.out.println(json);

        assertEquals("""
                {
                    "currency": "USD",
                    "amount": 100
                }""", json);
    }

    @Test
    void renderTemplate2() {
        var render = new TemplateRenderer();
        var bizContent = new HashMap<String, Object>();
        bizContent.put("out_trade_no", "70501111111S001111119");
        bizContent.put("total_amount", 9.00);
        bizContent.put("subject", "大乐透");
        bizContent.put("body", "对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。");
        bizContent.put("timeout_express", "90m");

        var json = render.renderTemplate("""
                {
                  "out_trade_no": "[[${out_trade_no}]]",
                  "total_amount": [[${total_amount}]],
                  "subject": "[[${subject}]]",
                  "body": "[[${body}]]",
                  "timeout_express": "[[${timeout_express}]]"
                }""", bizContent);

        System.out.println(json);

        assertEquals("""
                {
                  "out_trade_no": "70501111111S001111119",
                  "total_amount": 9.0,
                  "subject": "大乐透",
                  "body": "对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。",
                  "timeout_express": "90m"
                }""", json);
    }
}