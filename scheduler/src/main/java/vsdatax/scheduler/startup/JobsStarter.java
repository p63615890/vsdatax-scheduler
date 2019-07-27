package vsdatax.scheduler.startup;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.jobconf.JobConfKey;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.schedule.IJobManager;

import java.util.*;

/**
 * 启动配置里的所有Job，并根据job的创建时间判断是否更新Job.
 *
 * @author JerryHuang
 * Create Time: 2019/6/5
 */
public class JobsStarter {
    private static Logger logger = LoggerFactory.getLogger(JobsStarter.class);

    public static void invokeAllJobs(Scheduler scheduler, IJobConfMgr jobConfMgr, IJobManager jobManager, Class<? extends Job> jobClass) {
        Map<String, JobContext> quartzJobs = fetchAllJobsInQuartz(scheduler);
        Collection<JobConfModel> jobConfs = jobConfMgr.listJobConfs();
        String jobId = null;
        JobContext context = null;
        for (JobConfModel jobConfModel : jobConfs) {
            jobId = jobConfModel.getJobConfParams().get(JobConfKey.KEY_JOBID);
            context = JobContexHelper.load(jobConfModel);
            if (!quartzJobs.containsKey(jobId)) {
                //新任务.
                jobManager.createJob(scheduler, context, jobClass);
            } else {
                if (context.getLastModified() < jobConfModel.getLastModified()) {
                    //The job configurations have been changed ,but quartz have not update its managing job.
                    jobManager.updateJob(scheduler, context, jobClass);
                }

            }
        }
    }

    private static Map<String, JobContext> fetchAllJobsInQuartz(Scheduler scheduler) {
        Map<String, JobContext> contextMap = new HashMap<>();
        try {
            List<String> jobGroupNames = scheduler.getJobGroupNames();
            for (String groupName : jobGroupNames) {
                Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                for (JobKey jobKey : jobKeys) {
                    String jobName = jobKey.getName();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    JobContext jobContext = (JobContext) jobDetail.getJobDataMap().get("jobContext");
                    String jobId = jobContext.getDataxJobMeta().getJobId();
                    contextMap.put(jobId, jobContext);
                    logger.info("[jobName] : {}, [groupName] :{},[jobId]:{} ", jobName, groupName, jobId);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return contextMap;
    }


}
