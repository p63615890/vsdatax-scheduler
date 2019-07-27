package vsdatax.scheduler.job.datax;

import vsincr.jobstatus.JobStatus;

import java.io.Serializable;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class DataxExecuteResult  implements Serializable {
    private static final long serialVersionUID = -356282192608847804L;
    boolean success = false;
    JobStatus currentStatus = null;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JobStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(JobStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
