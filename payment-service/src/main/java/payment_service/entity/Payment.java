package payment_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "user_id" , nullable = false)
    private UUID userId ;

    @Column(name = "wallet_id" , nullable = false)
    private UUID walletId ;

    @Column(nullable = false , precision = 19 , scale = 4)
    private BigDecimal amount ;

    @Column(nullable = false ,length = 3)
    private String currency ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private PaymentStatus status ;

    @Column(nullable = false , unique = true , length = 100)
    private String reference ;

    @Column(name = "created_at" , nullable = false)
    private OffsetDateTime createdAt ;

    @Column(name = "updated_at" ,nullable = false)
    private OffsetDateTime updatedAt ;

    @Column(name = "idempotency_key",nullable = false,unique = true)
    private String idempotencyKey ;

    protected Payment(){}

    public Payment(UUID userId, UUID walletId, BigDecimal amount, String currency, PaymentStatus status, String reference,String idempotencyKey, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.userId = userId;
        this.walletId = walletId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.reference = reference;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.idempotencyKey = idempotencyKey ;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
