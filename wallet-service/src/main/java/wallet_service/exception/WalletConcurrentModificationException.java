package wallet_service.exception;

public class WalletConcurrentModificationException extends RuntimeException{
    public WalletConcurrentModificationException(String message)
    {
        super(message) ;
    }
}
