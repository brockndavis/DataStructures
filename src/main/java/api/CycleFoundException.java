package api;

public class CycleFoundException extends RuntimeException{

    public CycleFoundException() {
    }

    public CycleFoundException(String message) {
        super(message);
    }

    public CycleFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CycleFoundException(Throwable cause) {
        super(cause);
    }

    public CycleFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
