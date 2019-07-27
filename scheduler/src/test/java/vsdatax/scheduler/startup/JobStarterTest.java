package vsdatax.scheduler.startup;

import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.WorkEnvHelper;
import vsdatax.scheduler.startup.BootstrapException;

import java.util.Date;
import java.util.List;

/**
 * @author JerryHuang
 * Create Time: 2019/6/6
 */
public class JobStarterTest {
    Scheduler scheduler = null;

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

    @Test
    public void testFetchQuartzJobs() {
        fetchQuartzJobs(scheduler);

    }

    public void fetchQuartzJobs(Scheduler scheduler) {
        try {
            List<String> jobGroupNames = scheduler.getJobGroupNames();
            for (String groupName : jobGroupNames) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    System.out.println("jobDetail:" + jobDetail.getKey());
                    JobContext jobContext = (JobContext) jobDetail.getJobDataMap().get("jobContext");
                    System.out.println("jobconext id:" + jobContext.getDataxJobMeta().getJobId());
                    System.out.println("jobstatus configuration is  lastModified;:" + jobContext.getLastModified());
                    //get jobstatus's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);

                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    System.out.println("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
