package wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import wallet_service.entity.Wallet;
import wallet_service.entity.WalletStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WalletResponse(
       UUID id ,
       UUID userId ,
       String currency ,
       BigDecimal balance ,
       WalletStatus status ,
       OffsetDateTime createdAt ,
       OffsetDateTime updatedAt
) {

    public static WalletResponse from(Wallet wallet){
        return new WalletResponse(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getCurrency(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );


    }
}
