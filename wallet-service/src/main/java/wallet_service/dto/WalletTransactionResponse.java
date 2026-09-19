package wallet_service.dto;

import wallet_service.entity.TransactionType;
import wallet_service.entity.WalletTransaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WalletTransactionResponse (
        UUID id ,
        TransactionType type ,
        BigDecimal amount ,
        BigDecimal balanceAfter ,
        OffsetDateTime createdAt
){

    public static WalletTransactionResponse from(WalletTransaction transaction){
        return new WalletTransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getCreatedAt()
        );
    }
}
