package vsdatax.scheduler.job.datax;

import com.alibaba.datax.core.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vsdatax.scheduler.context.DataxJobMeta;

import java.util.Map;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class ThreadDataxExecutor implements IDataxExecutor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public DataxExecuteResult invokeJob(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs) {
        String jobScriptFile = dataxJobMeta.getJobScriptFile();
        System.setProperty("datax.home", dataxJobMeta.getDataxHome());
        for (Map.Entry<String, String> entry : sysEnvs.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
        boolean success = true;
        String[] datxArgs = {"-job", jobScriptFile, "-mode", "standalone", "-jobid", dataxJobMeta.getJobId()};
        try {
            Engine.entry(datxArgs);
        } catch (Throwable e) {
            logger.error(StackTraceUtils.getStackTrace(e));
            success = false;
        }
        return DataxHelper.getExecuteResult(sysEnvs, success);
    }
}
