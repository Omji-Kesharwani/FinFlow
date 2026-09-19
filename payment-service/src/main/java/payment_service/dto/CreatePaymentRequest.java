package payment_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount ,

        @NotBlank(message = "Currency is required")
        @Pattern(
                regexp = "INR|USD|EUR",
                message = "Currency must be INR , USD or EUR"
        )
        String currency

) {
}
