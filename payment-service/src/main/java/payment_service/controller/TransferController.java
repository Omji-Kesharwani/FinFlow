package payment_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment_service.dto.TransferDto;
import payment_service.entity.Transfer;
import payment_service.exception.IdempotencyKeyMissingException;
import payment_service.service.TransferService;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments/transfers")
public class TransferController {

    private final TransferService transferService ;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(Authentication authentication,
                                                   HttpServletRequest request,
                                                   @Valid @RequestBody TransferDto transferDto)
    {
        UUID senderUserId =
                (UUID) authentication.getPrincipal();

        String authHeader =
                request.getHeader("Authorization");


        String token =
                authHeader.substring(7);

        String idempotencyKey =
                request.getHeader("Idempotency-Key");

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IdempotencyKeyMissingException(
                    "Idempotency-Key header is required"
            );
        }

            Transfer transfer =
                    transferService.createTransfer(
                            senderUserId,
                            token,
                            idempotencyKey,
                            transferDto
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(transfer);
        }
    }
