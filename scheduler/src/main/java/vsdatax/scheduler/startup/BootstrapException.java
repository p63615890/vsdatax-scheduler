package vsdatax.scheduler.startup;

/**
 * @author JerryHuang
 */
public class BootstrapException extends RuntimeException {
    public BootstrapException() {
    }

    public BootstrapException(String message) {
        super(message);
    }

    public BootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public BootstrapException(Throwable cause) {
        super(cause);
    }

    public BootstrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
