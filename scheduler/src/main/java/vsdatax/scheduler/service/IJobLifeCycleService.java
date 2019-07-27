package vsdatax.scheduler.service;

import vsincr.jobstatus.IJobStatusMgr;
import vsdatax.scheduler.job.JobInfo;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.schedule.IJobManager;

import java.util.List;

/**
 * 管理Job生命周期的Service接口
 * User: JerryHuang
 */
public interface IJobLifeCycleService {

    void init(IJobConfMgr jobConfMgr, IJobStatusMgr jobStatusMgr, IJobManager jobManager);

    void createJob(JobConfModel jobConf);

    void updateJob(JobConfModel jobConf);

    void createOrUpdateJob(JobConfModel jobConf);

    void removeJob(String jobId, String jobGroupName);


    boolean pauseJob(String jobName, String groupName);

    boolean resumeJob(String jobName, String groupName);

    boolean triggerJob(String jobName, String groupName);

    JobInfo getJobInfo(String jobGroupName, String jobId);

    List<JobInfo> listJob();
}
