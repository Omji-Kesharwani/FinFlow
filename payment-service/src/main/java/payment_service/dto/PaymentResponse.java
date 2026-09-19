package payment_service.dto;

import payment_service.entity.Payment;
import payment_service.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id ,
        UUID userId ,
        UUID walletId ,
        BigDecimal amount ,
        String currency ,
        PaymentStatus status ,
        String reference ,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static PaymentResponse from(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getWalletId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getReference(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
