package vsdatax.scheduler.context;

import vsdatax.scheduler.jobconf.JobConfModel;

/**
 * @author JerryHuang
 * Create Time: 2019/6/14
 */
public class JobContexHelper {
    public static JobContext load(JobConfModel jobConfModel) {
        JobContext context = new JobContext(jobConfModel.getJobConfParams());
        context.setLastModified(jobConfModel.getLastModified());
        return context;
    }
}
