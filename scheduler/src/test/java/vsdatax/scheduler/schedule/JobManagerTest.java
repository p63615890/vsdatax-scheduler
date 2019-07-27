package vsdatax.scheduler.schedule;

import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vsdatax.scheduler.startup.BootstrapException;
import vsdatax.scheduler.env.WorkEnvHelper;

/**
 * User: JerryHuang
 */
public class JobManagerTest  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    Scheduler scheduler = null;
    @Test
    public void testRemoveJob() {
        IJobManager jobManager =new DataxJobManager();
        jobManager.removeJob(scheduler,"job_demo1","job.group1");
        jobManager.removeJob(scheduler,"job_demo_num","job.group1");
        jobManager.removeJob(scheduler,"job_demo_num_company","job.group1");
        jobManager.removeJob(scheduler,"job_demo_onlytime","job.group1");
        jobManager.removeJob(scheduler,"job_demo_only_sysvar","job.group1");
    }


    @Before
    public void init() {
        WorkEnvHelper.init("D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf");
        String quartzProperties = WorkEnvHelper.getQuartzFile();


        try {
            scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BootstrapException(e);
        }
    }

}
