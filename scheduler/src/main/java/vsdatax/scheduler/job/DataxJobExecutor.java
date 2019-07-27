package vsdatax.scheduler.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vsdatax.scheduler.context.DataxJobMeta;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.job.datax.*;
import vsincr.jobstatus.IJobStatusMgr;
import vsincr.jobstatus.JobStatus;
import vsincr.variable.IIncrEnvGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Datax Job执行器。
 * 主要过程：通过从作业元数据配置文件里读取配置信息，结合上次Job运行状态，经过解析，提取出需要的变量，然后直接执行
 * datax jobstatus。
 *
 * @author JerryHuang
 */
public class DataxJobExecutor {
    private static Logger logger = LoggerFactory.getLogger(DataxJobExecutor.class);


    public static boolean execute(IIncrEnvGenerator incEnvGenerator, IJobStatusMgr jobStatusMgr, JobContext jobContext, Map<String, Object> params) {
        String jobId = jobContext.getDataxJobMeta().getJobId();
        JobStatus lastJobStatus = jobStatusMgr.fetchJobStatus(jobContext.getJobStatusConf());
        Map<String, Object> vars = incEnvGenerator.resolveAll(jobContext.getIncrEnvConf(), lastJobStatus);
        DataxExecuteResult executeResult = doExecute(jobContext.getDataxJobMeta(), vars, params);
        if (executeResult.isSuccess()) {
            //Use sys envs as current status.
            jobStatusMgr.createOrUpdJobStatus(jobContext.getJobStatusConf(), executeResult.getCurrentStatus());
        } else {
            logger.warn("The job didn't execute successfully,So rollback the status");
        }
        return executeResult.isSuccess();
    }


    private static DataxExecuteResult doExecute(DataxJobMeta dataxJobMeta, Map<String, Object> vars, Map<String, Object> params) {
        Map<String, String> sysEnvs = new HashMap<>();
        //TODO var的值可能有多种类型，当前暂时只按String类型处理。
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            sysEnvs.put(entry.getKey(), entry.getValue().toString());
        }

        DataxExecuteResult executeResult = null;

        IDataxExecutor dataxExecutor = null;
        if (dataxJobMeta.getInvokeWay().equalsIgnoreCase(JobConstants.INVOKE_WAY_PROCESS)) {
            dataxExecutor = new ProcessorDataxExecutor();

        } else if (dataxJobMeta.getInvokeWay().equalsIgnoreCase(JobConstants.INVOKE_WAY_THREAD)) {
            dataxExecutor = new ThreadDataxExecutor();
        } else if (dataxJobMeta.getInvokeWay().equalsIgnoreCase(JobConstants.INVOKE_WAY_SSH)) {
            dataxExecutor = new SSHDataxExecutor();
        } else {
            dataxExecutor = new ProcessorDataxExecutor();
        }


        executeResult = dataxExecutor.invokeJob(dataxJobMeta,sysEnvs);
        return executeResult;
    }


}
