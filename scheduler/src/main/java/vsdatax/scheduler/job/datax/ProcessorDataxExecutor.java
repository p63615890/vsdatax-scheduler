package vsdatax.scheduler.job.datax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vscommons.vsutils.str.VsStringUtils;
import vsdatax.scheduler.context.DataxJobMeta;
import vsdatax.scheduler.utils.RuntimeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * 基于进程的Datax Job执行器
 *
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class ProcessorDataxExecutor implements IDataxExecutor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public DataxExecuteResult invokeJob(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs) {
        String[] dataxArgs = DataxHelper.extractDataxArgs(dataxJobMeta, sysEnvs);
        String cmd = VsStringUtils.array2Str(dataxArgs, " ");
        if (logger.isDebugEnabled()) {
            logger.debug(cmd);
        }


        int exitCode = 0;
        int failsNum = -1;
        try {
            Process process = null;
            //  TODO:暂时不处理非windows和linux操作系统
            if (RuntimeUtils.isWindows()) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), dataxJobMeta.getEnvEncoding()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("读写失败总数")) {
                    failsNum = DataxHelper.extractFailedNumFromLocal(line);
                }
            }
            exitCode = process.waitFor();
        } catch (Exception e) {
            exitCode = 1;
            logger.error(StackTraceUtils.getStackTrace(e));
        }
        logger.info("Proccess Exit Value:" + exitCode);
        boolean success = exitCode == 0 && failsNum == 0;
        return DataxHelper.getExecuteResult(sysEnvs, success);
    }
    /* public DataxExecuteResult invokeJob(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs) {
        String[] dataxArgs = DataxHelper.extractDataxArgs(dataxJobMeta,sysEnvs);

        if (logger.isDebugEnabled()){
            logger.debug(VsStringUtils.array2Str(dataxArgs, " "));
        }


        ProcessBuilder builder = new ProcessBuilder(dataxArgs);
        Process process = null;

        int exitCode = 0;
        int failsNum = -1;
        try {
            process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), dataxJobMeta.getEnvEncoding()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("读写失败总数")) {
                    failsNum = DataxHelper.extractFailedNumFromLocal(line);
                }
            }
            exitCode = process.waitFor();
        } catch (Exception e) {
            exitCode = 1;
            logger.error(StackTraceUtils.getStackTrace(e));
        }
        logger.info("Proccess Exit Value:" + exitCode);
        boolean success = exitCode == 0 && failsNum == 0;
        return DataxHelper.getExecuteResult(sysEnvs, success);
    }*/
}
