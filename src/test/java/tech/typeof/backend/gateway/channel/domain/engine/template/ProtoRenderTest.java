package tech.typeof.backend.gateway.channel.domain.engine.template;

import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.infrastructure.engine.template.TemplateRenderer;

import java.util.HashMap;

class ProtoRenderTest {

    @Test
    void renderTemplate() {
        var render = new TemplateRenderer();
        var map = new HashMap<String, Object>();
        map.put("currency", "USD");
        map.put("amount", 100);

        var json = render.renderTemplate("{\n" +
                              "    \"currency\": \"[[${currency}]]\",\n" +
                              "    \"amount\": [[${amount}]]\n" +
                              "}", map);

        System.out.println(json);
    }
}