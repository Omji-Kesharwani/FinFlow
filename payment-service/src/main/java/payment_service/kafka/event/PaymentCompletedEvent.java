package payment_service.kafka.event;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCompletedEvent(
        UUID paymentId ,
        UUID userId ,
        UUID walletId ,
        BigDecimal amount ,
        String currency ,
        String reference,
        String status
) {
}
