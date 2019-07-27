package vsdatax.scheduler.jobconf;

/**
 * @author JerryHuang
 * Create Time: 2019/6/8
 */
public class JobConfException extends  RuntimeException  {
    public final static String CODE_FAIL_DELETED="1";
    public final static String CODE_FAIL_UPDATE="2";
    private String failCode=null;
    public JobConfException() {
    }

    public JobConfException(String message) {
        super(message);
    }

    public JobConfException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobConfException(Throwable cause) {
        super(cause);
    }

    public JobConfException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getFailCode() {
        return failCode;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
}
