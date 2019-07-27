package vsdatax.scheduler.facade;

import vscommons.vsutils.io.PropertiesCfgUtils;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.startup.SingleJobInvokeHelper;
import vsincr.jobstatus.IJobStatusMgr;

import java.util.Map;
import java.util.Properties;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class JobInvokerByFileFacade {

    public static void invoke(String jobConfFile, Map<String, Object> jobParams, long lastModified) {
        innerInvoke(jobConfFile, jobParams, lastModified);
    }

    private static void innerInvoke(String jobConfFile, Map<String, Object> jobParams, long lastModified) {
        IJobStatusMgr jobStatusMgr  = BcmFactory.getInstance().getBean("jobStatusMgr",IJobStatusMgr.class);
        Properties properties = PropertiesCfgUtils.loadProps(jobConfFile);
        JobConfModel jobConfModel = new JobConfModel();
        jobConfModel.load(PropertiesCfgUtils.prop2Map(properties), lastModified);
        JobContext jobContext = JobContexHelper.load(jobConfModel);
        jobContext.setLastModified(lastModified);

        SingleJobInvokeHelper.invoke(jobStatusMgr,jobContext, jobParams);
    }
}
