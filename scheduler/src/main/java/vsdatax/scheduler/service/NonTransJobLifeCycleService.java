package vsdatax.scheduler.service;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.job.JobInfo;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.schedule.DataxQuartzJob;
import vsdatax.scheduler.schedule.IJobManager;
import vsdatax.scheduler.schedule.SchedulerHolder;
import vsincr.conf.JobStatusConf;
import vsincr.jobstatus.IJobStatusMgr;
import vsincr.jobstatus.JobStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基于File存储的JOB任务管理器实现
 * User: JerryHuang
 */
public class NonTransJobLifeCycleService implements IJobLifeCycleService {
    private Logger logger = LoggerFactory.getLogger(getClass());


    private IJobConfMgr jobConfMgr;
    private IJobManager jobManager;
    private IJobStatusMgr jobStatusMgr;

    @Override
    public void init(IJobConfMgr jobConfMgr, IJobStatusMgr jobStatusMgr, IJobManager jobManager) {
        this.jobConfMgr = jobConfMgr;

        this.jobManager = jobManager;
        this.jobStatusMgr = jobStatusMgr;
    }

    @Override
    public void createJob(JobConfModel jobConf) {

        JobContext jobContext = JobContexHelper.load(jobConf);
        jobConfMgr.addJobConf(jobConf);
        //invoke job trigger
        TriggerKey triggerKey = jobManager.createJob(SchedulerHolder.getScheduler(), jobContext, DataxQuartzJob.class);


    }

    @Override
    public void updateJob(JobConfModel jobConf) {
        JobContext jobContext = JobContexHelper.load(jobConf);
        jobConfMgr.updateJobConf(jobConf);
        //invoke jobstatus trigger
        TriggerKey triggerKey = jobManager.updateJob(SchedulerHolder.getScheduler(), jobContext, DataxQuartzJob.class);
        //TODO:更新job有可能导致原先的运行状态数据和现有的不一致，因此可能需要更细粒度地对状态进行处理。
    }

    @Override
    public void createOrUpdateJob(JobConfModel jobConf) {
        jobConfMgr.createOrUpdateJobConf(jobConf);
        JobContext jobContext = JobContexHelper.load(jobConf);
        TriggerKey triggerKey = jobManager.updateJob(SchedulerHolder.getScheduler(), jobContext, DataxQuartzJob.class);
    }

    @Override
    public void removeJob(String jobId, String jobGroupName) {
        JobContext jobContext = getJobContext(jobId, jobGroupName);
        JobStatusConf jobStatusConf=jobContext.getJobStatusConf();

        //get job configuration
        removeJobFromScheduler(SchedulerHolder.getScheduler(), jobId, jobGroupName);
        jobConfMgr.removeJobConf(jobId);
        jobStatusMgr.removeJobStatus(jobStatusConf);
    }


    private void removeJobFromScheduler(Scheduler scheduler, String jobId, String jobGroupName) {
        logger.debug("begin to remove jobstatus:{}", jobId);
        jobManager.removeJob(scheduler, jobId, jobGroupName);

    }

    public JobInfo getJobInfo(String groupName, String jobId) {
        Scheduler scheduler = SchedulerHolder.getScheduler();
        JobInfo jobInfo = null;
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(jobId, groupName);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            jobInfo = new JobInfo();
            jobInfo.setJobGroup(groupName);
            jobInfo.setJobId(jobId);
            jobInfo.setNextFireTime(trigger.getNextFireTime());
            Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
            if (state != null) {
                String statusStr = state.toString();
                jobInfo.setJobStatus(statusStr);
            }

        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            jobInfo = null;
        }
        return jobInfo;
    }

    public List<JobInfo> listJob() {
        Scheduler scheduler = SchedulerHolder.getScheduler();
        List<JobInfo> all = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames())
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    //get jobstatus's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    logger.info("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime);
                    JobInfo jobInfo = new JobInfo();
                    jobInfo.setJobGroup(jobGroup);
                    jobInfo.setJobId(jobName);
                    jobInfo.setNextFireTime(nextFireTime);
                    Trigger.TriggerState state = scheduler.getTriggerState(triggers.get(0).getKey());
                    if (state != null) {
                        String statusStr = state.toString();
                        jobInfo.setJobStatus(statusStr);
                    }
                    all.add(jobInfo);
                }
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            all = null;
        }
        return all;
    }

    /**
     * 根据任务名称跟分组名称来暂停任务，暂停抛出异常则暂停任务失败
     *
     * @param jobName
     * @param groupName
     * @return
     */
    public boolean pauseJob(String jobName, String groupName) {
        Scheduler scheduler = SchedulerHolder.getScheduler();
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        boolean isPaused = false;
        try {
            scheduler.pauseJob(jobKey);
            isPaused = true;
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            isPaused = false;
        }
        return isPaused;
    }

    /**
     * 恢复任务执行
     *
     * @param jobName
     * @param groupName
     * @return
     */
    public boolean resumeJob(String jobName, String groupName) {
        Scheduler scheduler = SchedulerHolder.getScheduler();
        boolean isResumed = false;
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        try {
            scheduler.resumeJob(jobKey);
            isResumed = true;
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            isResumed = false;
        }
        return isResumed;
    }

    /**
     * 任务立即执行一次。这里的立即运行，只会运行一次，方便测试时用。quartz是通过临时生成一个trigger的方式来实现的，这个trigger将在本次任务运行完成之后自动删除。
     *
     * @param jobName
     * @param groupName
     * @return
     */
    public boolean triggerJob(String jobName, String groupName) {
        Scheduler scheduler = SchedulerHolder.getScheduler();
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        boolean isExecuted = false;
        try {
            scheduler.triggerJob(jobKey);
            isExecuted = true;
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            isExecuted = false;
        }
        return isExecuted;
    }

    private JobContext getJobContext(String jobId, String jobGroupName) {
        JobKey jobKey = new JobKey(jobId, jobGroupName);
        JobContext jobContext = null;
        Scheduler scheduler=SchedulerHolder.getScheduler();
        try {
            JobDataMap jobDataMap=scheduler.getJobDetail(jobKey).getJobDataMap();
            jobContext = (JobContext)jobDataMap.get("jobContext");
        } catch (Exception e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new RuntimeException(e);

        }
        return jobContext;
    }
}
