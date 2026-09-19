package payment_service.exception;

public class IdempotencyKeyMissingException  extends  RuntimeException {
    public IdempotencyKeyMissingException(String message)
    {
        super(message) ;
    }
}
