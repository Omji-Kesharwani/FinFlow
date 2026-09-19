package payment_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import payment_service.entity.Payment;
import payment_service.entity.PaymentStatus;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

//    Optional<Payment> findByReference(String reference) ;

    Page<Payment> findByUserIdOrderByCreatedAtDesc(UUID userId , Pageable pageable) ;

//    Page<Payment> findByUserIdAndStatusOrderByCreatedAtDesc(
//            UUID userId,
//            PaymentStatus status ,
//            Pageable pageable
//    );

    Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId) ;

    Optional<Payment> findByUserIdAndIdempotencyKey(UUID userId,String idempotencyKey) ;
}
