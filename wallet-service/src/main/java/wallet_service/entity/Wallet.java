package wallet_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "wallets",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wallet_user_id" , columnNames = "user_id")
        }
)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "user_id" ,nullable = false , unique = true)
    private UUID userId ;

    @Column(nullable = false,length = 3)
    private String currency ;

    @Column(nullable = false,precision = 19 , scale = 4)
    private BigDecimal balance ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private WalletStatus status ;

    @Version
    @Column(nullable = false)
    private Long version ;

    @Column(name = "created_at" , nullable = false)
    private OffsetDateTime createdAt ;

    @Column(name = "updated_at" , nullable = false)
    private OffsetDateTime updatedAt ;

    protected Wallet(){

    }

    public Wallet( UUID userId, String currency, BigDecimal balance, WalletStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public UUID getId() {
        return id;
    }



    public UUID getUserId() {
        return userId;
    }



    public String getCurrency() {
        return currency;
    }


    public BigDecimal getBalance() {
        return balance;
    }



    public WalletStatus getStatus() {
        return status;
    }



    public Long getVersion() {
        return version;
    }



    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }



    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
