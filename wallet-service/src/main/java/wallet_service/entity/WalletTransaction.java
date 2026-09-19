package wallet_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_wallet_transaction_wallet_reference",
                columnNames = {"wallet_id","reference"}
        )
        })
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "wallet_id" , nullable = false)
    private UUID walletId ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private TransactionType type ;

    @Column( nullable = false , precision = 19 , scale = 4)
    private BigDecimal amount ;

    @Column(name = "balance_after" ,nullable = false,precision = 19 ,scale = 4)
    private BigDecimal balanceAfter ;

    @Column(name = "created_at",nullable = false)
    private OffsetDateTime createdAt ;

    @Column(name = "reference" , nullable = false,length = 100)
    private String reference ;

    protected WalletTransaction(){

    }

    public WalletTransaction(UUID walletId, TransactionType type, BigDecimal amount, BigDecimal balanceAfter, OffsetDateTime createdAt,String reference) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.createdAt = createdAt;
        this.reference = reference ;
    }

    public UUID getId() {
        return id;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getReference()
    {
        return reference ;
    }
}
