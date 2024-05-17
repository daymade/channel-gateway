package tech.typeof.backend.gateway.channel.adapter.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayPaymentResponse {
    private String status;
    private String transactionId;
    private String message;
}
