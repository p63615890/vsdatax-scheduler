package vsdatax.scheduler.startup;

import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.job.DataxJobExecutor;
import vsincr.jobstatus.IJobStatusMgr;
import vsincr.variable.DefaultIncrEnvGenerator;
import vsincr.variable.IIncrEnvGenerator;

import java.util.Map;

/**
 * @author JerryHuang
 */
public class SingleJobInvokeHelper {

    public static void invoke(IJobStatusMgr jobStatusMgr,JobContext jobContext, Map<String,Object> jobParams) {

        IIncrEnvGenerator incrEnvGenerator=new DefaultIncrEnvGenerator();
        DataxJobExecutor.execute(incrEnvGenerator,jobStatusMgr,jobContext,jobParams);
    }

}
