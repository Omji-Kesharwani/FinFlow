package payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment_service.entity.Transfer;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
}
