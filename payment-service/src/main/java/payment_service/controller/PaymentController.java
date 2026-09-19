package payment_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import payment_service.dto.CreatePaymentRequest;
import payment_service.dto.PageRequestDto;
import payment_service.dto.PaymentResponse;
import payment_service.entity.Payment;
import payment_service.exception.IdempotencyKeyMissingException;
import payment_service.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService ;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(Authentication authentication
            , HttpServletRequest request , @Valid @RequestBody CreatePaymentRequest paymentRequest)
    {
//        System.out.println("========== CONTROLLER HIT ==========");
//        System.out.println("Authentication: " + authentication);
//        System.out.println("Principal: " + authentication.getPrincipal());

        UUID userId = (UUID) authentication.getPrincipal() ;
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        String idempotencyKey = request.getHeader("Idempotency-Key");

        if(idempotencyKey == null || idempotencyKey.isBlank())
        {
            throw new IdempotencyKeyMissingException("Idempotency-Key header is required");
        }

        Payment payment = paymentService.createPayment(userId,token ,idempotencyKey,paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentResponse.from(payment));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse>getPayment(Authentication authentication , @PathVariable UUID paymentId)
    {
        UUID userId = (UUID) authentication.getPrincipal() ;
        Payment payment = paymentService.getPaymentById(userId,paymentId);

        return ResponseEntity.ok(
                PaymentResponse.from(payment)
        );
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getPayments(
            Authentication authentication ,
            @Valid @ModelAttribute PageRequestDto pageRequest
            ){
        UUID userId = (UUID) authentication.getPrincipal() ;
        Pageable pageable = PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize()
        );

        return ResponseEntity.ok(
                paymentService.getPayments(
                        userId , pageable
                )
        );
    }
}
