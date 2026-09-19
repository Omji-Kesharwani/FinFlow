package payment_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "transfers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transfer_idempotency_key",
                        columnNames = "idempotency_key"
                ),
                @UniqueConstraint(
                        name = "uk_transfer_reference",
                        columnNames = "reference"
                )
        }
)
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sender_user_id", nullable = false)
    private UUID senderUserId;

    @Column(name = "receiver_user_id", nullable = false)
    private UUID receiverUserId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferStatus status;

    @Column(nullable = false, unique = true, length = 100)
    private String reference;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Transfer() {}

    public Transfer(UUID senderUserId, UUID receiverUserId, BigDecimal amount, String currency, TransferStatus status, String reference, String idempotencyKey, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.reference = reference;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderUserId() {
        return senderUserId;
    }


    public UUID getReceiverUserId() {
        return receiverUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
