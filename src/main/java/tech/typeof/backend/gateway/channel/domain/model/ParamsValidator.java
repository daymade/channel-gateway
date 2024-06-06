package tech.typeof.backend.gateway.channel.domain.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.http.HttpStatus;
import tech.typeof.backend.gateway.channel.adapter.api.exception.ChannelGatewayException;

import java.util.Set;

public class ParamsValidator {

    public void validate(Object request) {
        try (var validator = Validation.buildDefaultValidatorFactory()) {
            Set<ConstraintViolation<Object>> violations = validator.getValidator().validate(request);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Object> violation : violations) {
                    sb.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append(", ");
                }

                // replace last , with ., do not overflow
                if (sb.length() > 2) {
                    sb.replace(sb.length() - 2, sb.length(), ".");
                }

                throw new ChannelGatewayException(HttpStatus.BAD_REQUEST, "Request validation failed: " + sb);
            }
        }
    }
}
