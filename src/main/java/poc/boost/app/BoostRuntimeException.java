package poc.boost.app;

public class BoostRuntimeException extends RuntimeException {

    public BoostRuntimeException() {
        super();
    }

    public BoostRuntimeException(String message) {
        super(message);
    }

    public BoostRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoostRuntimeException(Throwable cause) {
        super(cause);
    }

    protected BoostRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
