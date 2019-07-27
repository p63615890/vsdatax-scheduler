package vsdatax.scheduler.job;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JerryHuang
 * Create Time: 2019/6/8
 */
public class JobInfo  implements Serializable {
    private String jobId;
    private String jobGroup;
    /**
     *  NONE,NORMAL,PAUSED,COMPLETE,ERROR,BLOCKED;
     */
    private String  jobStatus;

    /**
     * 任务下次执行时间
     */

    private Date nextFireTime;


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
}
