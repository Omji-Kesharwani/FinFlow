package payment_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletTransferRequest(
        UUID receiverUserId,
        BigDecimal amount,
        String reference
) {
}
