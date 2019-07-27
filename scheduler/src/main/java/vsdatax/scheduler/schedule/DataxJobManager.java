package vsdatax.scheduler.schedule;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vsdatax.scheduler.context.JobContext;

import java.util.Set;

/**
 * 管理Job的创建、更新等操作
 */
public class DataxJobManager implements IJobManager {
    private Logger logger = LoggerFactory.getLogger(DataxJobManager.class);

    public Set<TriggerKey> getAllTriggerKeys(Scheduler scheduler) {
        Set<TriggerKey> keys = null;
        try {
            keys = scheduler.getTriggerKeys(GroupMatcher.<TriggerKey>anyGroup());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return keys;
    }
    public TriggerKey createJob(Scheduler scheduler, JobContext jobContext, Class<? extends Job> jobClass) {
        logger.debug("Begin to create  jobstatus:{}", jobContext.getDataxJobMeta().getJobId());
        TriggerKey triggerKey = null;
        try {
            triggerKey = TriggerKey.triggerKey(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(jobClass).requestRecovery()
                        .withIdentity(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName()).build();
                jobDetail.getJobDataMap().put("jobContext", jobContext);

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobContext.getDataxJobMeta().getCronExpression());
                scheduleBuilder.withMisfireHandlingInstructionDoNothing();
                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                logger.info("jobstatus-[{}] has been added successfully!",jobContext.getDataxJobMeta().getJobId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return triggerKey;
    }
    public TriggerKey updateJob(Scheduler scheduler, JobContext jobContext, Class<? extends Job> jobClass) {
        logger.debug("begin to   upsert jobstatus:{}", jobContext.getDataxJobMeta().getJobId());
        TriggerKey triggerKey = null;
        try {
            triggerKey = TriggerKey.triggerKey(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).requestRecovery()
                    .withIdentity(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName()).build();
            jobDetail.getJobDataMap().put("jobContext", jobContext);
            if (null != trigger) {

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobContext.getDataxJobMeta().getCronExpression());

                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();
                scheduler.addJob(jobDetail, true, true);

                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);

//                just remove and readd jobstatus
//
//                removeJob(scheduler,context);
//                JobDetail jobDetail = JobBuilder.newJob(jobClass)
//                        .withIdentity(context.getJobId(), context.getJobGroupName()).build();
//                jobDetail.getJobDataMap().put("jobContext", context);
//                createJob(scheduler,context,jobClass);
                logger.info("jobstatus-[{}] has been updated successfully!",jobContext.getDataxJobMeta().getJobId());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return triggerKey;

    }



    public TriggerKey createOrUpdateJob(Scheduler scheduler,JobContext jobContext, Class<? extends Job> jobClass) {
        logger.debug("begin to create or update jobstatus:{}", jobContext.getDataxJobMeta().getJobId());


        TriggerKey triggerKey = null;
        try {
            triggerKey = TriggerKey.triggerKey(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).requestRecovery()
                    .withIdentity(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName()).build();
            jobDetail.getJobDataMap().put("jobContext", jobContext);
            if (null == trigger) {


                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobContext.getDataxJobMeta().getCronExpression());
                scheduleBuilder.withMisfireHandlingInstructionDoNothing();
                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                logger.info("jobstatus-[{}] has been added successfully!",jobContext.getDataxJobMeta().getJobId());
            } else {

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobContext.getDataxJobMeta().getCronExpression());
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();
                scheduler.addJob(jobDetail, true, true);

                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
                logger.info("jobstatus-[{}] has been updated successfully!",jobContext.getDataxJobMeta().getJobId());

//                just remove and readd jobstatus
//
//                removeJob(scheduler,context);
//                JobDetail jobDetail = JobBuilder.newJob(jobClass)
//                        .withIdentity(context.getJobId(), context.getJobGroupName()).build();
//                jobDetail.getJobDataMap().put("jobContext", context);
//                createJob(scheduler,context,jobClass);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return triggerKey;
    }


    public void removeJob(Scheduler scheduler, JobContext jobContext) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName());
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(new JobKey(jobContext.getDataxJobMeta().getJobId(), jobContext.getDataxJobMeta().getJobGroupName()));
            logger.info("jobstatus-[{}] has been removed successfully!",jobContext.getDataxJobMeta().getJobId());
        } catch (Exception e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }


    }

    public void removeJob(Scheduler scheduler, String jobId, String groupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobId, groupName);
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(new JobKey(jobId, groupName));
            logger.info("job-[{}] has been removed successfully!",jobId);
        } catch (Exception e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }


    }
}
