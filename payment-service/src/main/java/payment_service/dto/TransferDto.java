package payment_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferDto {
    @NotNull(message = "Receiver user ID is required")
    private UUID receiverUserId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Currency type is required")
    private String currency ;

    public UUID getReceiverUserId() {
        return receiverUserId ;
    }

    public void setReceiverUserId(UUID receiverWalletId) {
        this.receiverUserId = receiverWalletId ;
    }

    public BigDecimal getAmount(){
        return amount ;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
