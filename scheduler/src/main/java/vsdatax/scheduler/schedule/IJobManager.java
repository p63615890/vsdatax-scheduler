package vsdatax.scheduler.schedule;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import vsdatax.scheduler.context.JobContext;

import java.util.Set;

/**
 */
public interface IJobManager {
    TriggerKey createOrUpdateJob(Scheduler scheduler, JobContext jobContext, Class<? extends Job> jobClass);

    TriggerKey updateJob(Scheduler scheduler, JobContext jobContext, Class<? extends Job> jobClass);

    TriggerKey createJob(Scheduler scheduler, JobContext jobContext, Class<? extends Job> jobClass);

    void removeJob(Scheduler scheduler,  JobContext jobContext);

    Set<TriggerKey> getAllTriggerKeys(Scheduler scheduler);

    void removeJob(Scheduler scheduler, String jobId, String groupName);


}

