package vsdatax.scheduler.schedule;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;

/**
 * @author JerryHuang
 */
public class DataxJobManagerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
     String quartzProperties = "D:/work/projs/incubator/vsetl/vsdatax/scheduler/vsdatax-scheduler/dataxConf" + "/quartz.properties";
    @Test
    public void testRemoveJob() {
        String jobGroup="job.meta.group1";
        String jobName="job.meta.demo1";
        removeJob(jobName,jobGroup);
    }
    public void removeJob(String jobId,String groupName) {

        Scheduler scheduler = null;

        try {
            scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
        IJobManager jobManager=new DataxJobManager();
        jobManager.removeJob(scheduler, jobId,groupName);

    }
}
