package tech.typeof.backend.gateway.channel.adapter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.typeof.backend.gateway.channel.adapter.api.ChannelGateway;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;
import tech.typeof.backend.gateway.channel.adapter.api.request.GatewayPaymentRequest;
import tech.typeof.backend.gateway.channel.adapter.api.response.GatewayPaymentResponse;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final ChannelGateway gatewayPay;

    @PostMapping("/pay")
    public ResponseEntity<GatewayPaymentResponse> pay(@RequestBody GatewayPaymentRequest request) {
        try {
            GatewayPaymentResponse response = gatewayPay.pay(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ChannelGatewayException e) {
            return new ResponseEntity<>(new GatewayPaymentResponse("FAILURE", null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ChannelGatewayException.class)
    public ResponseEntity<String> handleChannelGatewayException(ChannelGatewayException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}