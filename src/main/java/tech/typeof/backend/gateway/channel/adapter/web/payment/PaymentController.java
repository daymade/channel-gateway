package tech.typeof.backend.gateway.channel.adapter.web.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.typeof.backend.gateway.channel.adapter.api.ChannelGateway;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final ChannelGateway channelGateway;

    @PostMapping("/pay")
    public ResponseEntity<GatewayPaymentResponse<String>> pay(@RequestBody GatewayPaymentRequest request) {
        try {
            var response = channelGateway.pay(request);
            if (response.isSuccessful()){
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.internalServerError()
                    .body(response);
        } catch (ChannelGatewayException e) {
            log.error("channel gateway pay failed", e);

            return ResponseEntity.internalServerError()
                    .body(GatewayPaymentResponse.failure("payment failed, please try again later"));
        }
    }

    @ExceptionHandler(ChannelGatewayException.class)
    public ResponseEntity<String> handleChannelGatewayException(ChannelGatewayException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}