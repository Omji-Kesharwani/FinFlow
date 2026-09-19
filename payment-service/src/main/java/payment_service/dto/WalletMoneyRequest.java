package payment_service.dto;

import java.math.BigDecimal;

public record WalletMoneyRequest(
   BigDecimal amount
) {
}
