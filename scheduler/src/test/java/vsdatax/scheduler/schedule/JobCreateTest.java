package vsdatax.scheduler.schedule;

import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vscommons.vsutils.io.PropertiesCfgUtils;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.WorkEnvHelper;
import vsdatax.scheduler.jobconf.JobConfModel;

import java.util.Properties;

/**
 * @author JerryHuang
 */
public class JobCreateTest {
    private Logger logger = LoggerFactory.getLogger(JobCreateTest.class);

    @Test
    public void testJobCreate() {
        Scheduler scheduler = SchedulerHolder.getScheduler();


        if (scheduler == null) {
            throw new RuntimeException("scheduler should be started first");
        }
        String metaFile = "D:/work/projs/incubator/vsetl/vsdatax/scheduler/vsdatax-scheduler/dataxConf/jobs/job_demo1.job";
        Properties properties = PropertiesCfgUtils.loadProps(metaFile);
        JobConfModel jobConfModel =new JobConfModel();
        jobConfModel.load(PropertiesCfgUtils.prop2Map(properties),0);

        JobContext jobContext = JobContexHelper.load(jobConfModel);

        jobContext.setLastModified(System.currentTimeMillis());
        IJobManager jobManager = new DataxJobManager();
        jobManager.createJob(scheduler, jobContext, DataxQuartzJob.class);
        System.exit(0);
//        DataxJobExecutor.init(JobContextLoader.getJobContext());
//        DataxJobExecutor.execute( null);
    }

    @Before
    public void init() {
        WorkEnvHelper.init("D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf");
        Scheduler scheduler = null;
        String quartzProperties = WorkEnvHelper.getQuartzFile();
        try {
            scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
            SchedulerHolder.setScheduler(scheduler);
//            scheduler.start();
        } catch (Exception e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

}
