package vsdatax.scheduler.startup;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.env.EnvInitiator;
import vsdatax.scheduler.env.WorkEnvHelper;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.schedule.DataxQuartzJob;
import vsdatax.scheduler.schedule.IJobManager;
import vsdatax.scheduler.schedule.SchedulerHolder;

/**
 * Datax调度器启动入口程序
 *
 * @author JerryHuang
 */
public class DataxJobBootstrap {
    private static Logger logger = LoggerFactory.getLogger(DataxJobBootstrap.class);

    public static void main(String[] args) {

        startup();

    }

    public static void startup() {
        EnvInitiator.initEnv();

        IJobManager jobManager = BcmFactory.getInstance().getBean("jobManager", IJobManager.class);
        IJobConfMgr jobConfMgr = BcmFactory.getInstance().getBean("jobConfMgr", IJobConfMgr.class);
        Scheduler scheduler = getScheduer();
        JobsStarter.invokeAllJobs(scheduler, jobConfMgr, jobManager, DataxQuartzJob.class);

        startJobs(scheduler);
    }

    private static Scheduler getScheduer() {
        Scheduler scheduler = null;
        String quartzProperties = WorkEnvHelper.getQuartzFile();
        try {
            scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new BootstrapException(e);
        }
        SchedulerHolder.setScheduler(scheduler);
        return scheduler;
    }

    private static void startJobs(Scheduler scheduler) {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            throw new BootstrapException(e);
        }

    }


}
