package wallet_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wallet_service.entity.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
