package vsdatax.scheduler.job.datax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vscommons.vsutils.str.VsStringUtils;
import vsdatax.scheduler.context.DataxJobMeta;
import vsdatax.scheduler.utils.SSHUtils;
import vsdatax.scheduler.utils.RuntimeUtils;

import java.util.Map;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class SSHDataxExecutor implements IDataxExecutor {
    private static Logger logger = LoggerFactory.getLogger(SSHDataxExecutor.class);

    @Override
    public DataxExecuteResult invokeJob(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs) {
        String varRefSymbol = RuntimeUtils.getEnvRefSymbol(dataxJobMeta.getSshHost().getOs());
        String[] dataxArgs = DataxHelper.extractDataxArgs(dataxJobMeta, sysEnvs, varRefSymbol);
        String cmd = VsStringUtils.array2Str(dataxArgs, " ");
        int exitCode = 0;
        int failsNum = -1;
        try {
            String executeResultStr = SSHUtils.executeOnce(dataxJobMeta.getSshHost(), cmd, dataxJobMeta.getEnvEncoding());
            System.out.println(executeResultStr);
            if (executeResultStr.contains("读写失败总数")) {
                failsNum = DataxHelper.extractFailedNumFromSSH(executeResultStr);
            }

        } catch (Exception e) {

            exitCode = 1;
            logger.error(StackTraceUtils.getStackTrace(e));
        }
        logger.info("Proccess Exit Value:" + exitCode);
        boolean success = exitCode == 0 && failsNum == 0;
        return DataxHelper.getExecuteResult(sysEnvs, success);
    }


}