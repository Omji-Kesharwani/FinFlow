package payment_service.exception;

public class WalletOperationException extends RuntimeException{

    private final int status ;
    private final String code ;
    public WalletOperationException(String message, int status, String code){
        super(message);
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }


}
