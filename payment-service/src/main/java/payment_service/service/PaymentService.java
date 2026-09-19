package payment_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_service.client.WalletClient;
import payment_service.dto.CreatePaymentRequest;
import payment_service.dto.PaymentResponse;
import payment_service.entity.OutboxEvent;
import payment_service.entity.Payment;
import payment_service.entity.PaymentStatus;
import payment_service.exception.IdempotencyKeyConflictException;
import payment_service.exception.PaymentNotFoundException;
import payment_service.exception.WalletOperationException;
import payment_service.exception.WalletServiceException;
import payment_service.kafka.event.PaymentCompletedEvent;
import payment_service.repository.OutboxEventRepository;
import payment_service.repository.PaymentRepository;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository ;
     private final WalletClient walletClient ;
     private final OutboxEventRepository outboxEventRepository ;
     private final ObjectMapper objectMapper ;

    public PaymentService(PaymentRepository paymentRepository, WalletClient walletClient,OutboxEventRepository outboxEventRepository,ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.walletClient = walletClient ;
        this.outboxEventRepository = outboxEventRepository ;
        this.objectMapper = objectMapper ;
    }

    @Transactional
    public Payment createPayment(UUID userId , String token ,String idempotencyKey, CreatePaymentRequest request){

        Optional<Payment> existingPayment = paymentRepository.findByUserIdAndIdempotencyKey(userId,idempotencyKey);

        if(existingPayment.isPresent())
        {
            Payment payment = existingPayment.get();
//            System.out.println("DB amount: " + payment.getAmount());
//            System.out.println("Request amount: " + request.amount());
//            System.out.println("DB currency: " + payment.getCurrency());
//            System.out.println("Request currency: " + request.currency());
            if(payment.getAmount().compareTo(request.amount()) != 0 || !payment.getCurrency().equalsIgnoreCase(request.currency()))
            {
                throw new IdempotencyKeyConflictException("Idempotency-Key has already been used for a different payment");
            }

            return payment ;
        }

        UUID walletId = walletClient.getWalletIdForUser(token);
        OffsetDateTime now = OffsetDateTime.now() ;
        String reference = generateReference();

        Payment payment = new Payment(
                userId,
                walletId,
                request.amount(),
                request.currency(),
                PaymentStatus.PENDING,
                reference ,
                idempotencyKey,
                now,
                now
        );

        Payment savedPayment = paymentRepository.save(payment) ;

        try{
            walletClient.withdraw(token,request.amount());
            savedPayment.setStatus(PaymentStatus.COMPLETED);
            savedPayment.setUpdatedAt(OffsetDateTime.now());
            savedPayment = paymentRepository.save(savedPayment);

            OutboxEvent event = new OutboxEvent(
                    UUID.randomUUID(),
                    "PAYMENT",
                    savedPayment.getId(),
                    "PAYMENT_COMPLETED",
                    createPaymentEventPayload(savedPayment),
                    OffsetDateTime.now()
            );

            outboxEventRepository.save(event) ;

        }
        catch (WalletOperationException ex)
        {
            savedPayment.setStatus(PaymentStatus.FAILED);
            savedPayment.setUpdatedAt(OffsetDateTime.now());
            paymentRepository.save(savedPayment);
            throw ex ;
        }
        catch(WalletServiceException ex)
        {
            savedPayment.setStatus(PaymentStatus.FAILED);
            savedPayment.setUpdatedAt(OffsetDateTime.now());
            paymentRepository.save(savedPayment);
            throw ex ;
        }

        return savedPayment ;
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(UUID userId, UUID paymentId){
        Payment payment = findPaymentById(paymentId,userId) ;

        if(!payment.getUserId().equals(userId))
        {
            throw new PaymentNotFoundException("Payment not found ");
        }

        return payment ;
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPayments(UUID userId, Pageable pageable)
    {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(
                        userId,pageable
                )
                .map(PaymentResponse::from);
    }

    private Payment findPaymentById(UUID paymentId,UUID userId)
    {
        return paymentRepository.findByIdAndUserId(paymentId,userId)
                .orElseThrow(()->new PaymentNotFoundException("Payment not found "));
    }

    private String generateReference()
    {
        return "PAY-"+UUID.randomUUID()
                .toString()
                .replace("-","")
                .substring(0,12)
                .toUpperCase();
    }

    private String createPaymentEventPayload(Payment payment)
    {
        PaymentCompletedEvent event = new PaymentCompletedEvent(
                payment.getId(),
                payment.getUserId(),
                payment.getWalletId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getReference(),
                payment.getStatus().name()
        );

        try{
            return objectMapper.writeValueAsString(event);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Failed to serialize payment event",ex);
        }
    }


}
