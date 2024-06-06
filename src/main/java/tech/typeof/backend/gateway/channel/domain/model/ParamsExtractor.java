package tech.typeof.backend.gateway.channel.domain.model;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.infrastructure.codec.mapping.MappingRule;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParamsExtractor {
    public Map<String, Object> extractParams(Object request, List<MappingRule> mappings) {
        Map<String, Object> map = new HashMap<>();

        for (var rule : mappings) {
            Object value = null;
            try {
                var descriptor = BeanUtils.getPropertyDescriptor(request.getClass(), rule.getSource());
                if (descriptor == null || descriptor.getReadMethod() == null) {
                    if (rule.isRequired()) {
                        throw new ChannelGatewayException(HttpStatus.BAD_REQUEST,
                                "Required field %s is missing or not accessible in class %s".formatted(
                                        rule.getSource(), request.getClass().getName()));
                    }
                    value = rule.getDefaultValue();
                } else {
                    value = descriptor.getReadMethod().invoke(request);
                    if (value == null && rule.isRequired()) {
                        throw new ChannelGatewayException(HttpStatus.BAD_REQUEST,
                                "Required field [%s] is missing".formatted(rule.getSource()));
                    }
                    if (value == null) {
                        value = rule.getDefaultValue();
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error accessing property " + rule.getSource(), e);
            }

            map.put(rule.getTarget(), value);
        }

        return map;
    }
}
