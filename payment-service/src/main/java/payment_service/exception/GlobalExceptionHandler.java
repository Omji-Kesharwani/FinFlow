package payment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import payment_service.dto.ErrorResponse;


import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException exception)
    {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "PAYMENT_NOT_FOUND",
                        exception.getMessage(),
                        OffsetDateTime.now()
                ));
    }

    @ExceptionHandler(WalletServiceException.class)
    public ResponseEntity<ErrorResponse>handleWalletServiceException(WalletServiceException exception)
    {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "SERVICE_UNAVAILABLE",
                exception.getMessage(),
                OffsetDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response) ;
    }

    @ExceptionHandler(WalletOperationException.class)
    public ResponseEntity<ErrorResponse> handleWalletOperationException(WalletOperationException ex){
        ErrorResponse error = new ErrorResponse(
                ex.getStatus(),
                ex.getCode(),
                ex.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error) ;
    }

    @ExceptionHandler(IdempotencyKeyMissingException.class)
    public ResponseEntity<ErrorResponse> handleIdempotencyKeyMissingException(
            IdempotencyKeyMissingException ex){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "IDEMPOTENCY_KEY_REQUIRED",
                ex.getMessage(),
                OffsetDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error) ;
    }

    @ExceptionHandler(IdempotencyKeyConflictException.class)
    public ResponseEntity<ErrorResponse> handleIdempotencyKeyConflict(IdempotencyKeyConflictException ex)
    {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "IDEMPOTENCY_KEY_CONFLICT",
                ex.getMessage(),
                OffsetDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransfer(InvalidTransferException ex)
    {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "BAD_REQUEST",
                                ex.getMessage(),
                                OffsetDateTime.now()
                        )
                );
    }


}
