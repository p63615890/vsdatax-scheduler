package vsdatax.scheduler.job.datax;

import vsdatax.scheduler.context.DataxJobMeta;

import java.util.Map;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public interface IDataxExecutor {
    public DataxExecuteResult invokeJob(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs);
}
