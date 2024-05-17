package tech.typeof.backend.gateway.channel.domain.model;

import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class ParamsExtractor {
    public Map<String, Object> extractParams(GatewayPaymentRequest request) {
        var map = new HashMap<String, Object>();
        map.put("currency", request.getCurrency());
        map.put("amount", request.getAmount());
        return map;
    }
}
