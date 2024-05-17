package tech.typeof.backend.gateway.channel.infrastructure.engine.template;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

@Component
public class TemplateRenderer {
    private final TemplateEngine templateEngine = createTemplateEngine();

    private TemplateEngine createTemplateEngine() {
        var templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCacheable(false); // 禁用缓存以便处理动态模板

        var engine = new TemplateEngine();
        engine.setDialect(new StandardDialect()); // 显式设置 StandardDialect

        engine.setTemplateResolver(templateResolver);

        System.out.println("Template engine initialized with resolver: " + engine.getTemplateResolvers());
        return engine;
    }

    public String renderTemplate(String template, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);
        System.out.println("Rendering template with context variables: " + context.getVariableNames());

        System.out.println("Original template: " + template);

        for(var name: context.getVariableNames()) {
            System.out.printf("Context variables:  %s -> %s%n", name, context.getVariable(name));
        }
        var s = new TemplateSpec(template, TemplateMode.TEXT);
        String processedTemplate = templateEngine.process(s, context);
        System.out.println("Processed template: " + processedTemplate);
        return processedTemplate;
    }
}
