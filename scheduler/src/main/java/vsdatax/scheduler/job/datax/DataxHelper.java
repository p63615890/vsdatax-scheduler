package vsdatax.scheduler.job.datax;

import vsdatax.scheduler.context.DataxJobMeta;
import vsdatax.scheduler.utils.RuntimeUtils;
import vsincr.jobstatus.JobStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class DataxHelper {
    public static DataxExecuteResult getExecuteResult(Map<String, String> sysEnvs, boolean success) {
        DataxExecuteResult executeResult = new DataxExecuteResult();
        JobStatus currentStatus = new JobStatus();
        currentStatus.fromMap(sysEnvs);
        executeResult.setCurrentStatus(currentStatus);
        executeResult.setSuccess(success);
        return executeResult;
    }

    public static int extractFailedNumFromLocal(String logLine) {
        String[] tokens = logLine.split(":");
        int failedNum = Integer.parseInt(tokens[1].trim());
        return failedNum;
    }

    public static int extractFailedNumFromSSH(String log) {
        int i = log.indexOf("读写失败总数");
        String logline = log.substring(i, log.length());
        String[] tokens = logline.split(":");
        int failedNum = Integer.parseInt(tokens[1].trim());
        return failedNum;
    }

    public static String[] extractDataxArgs(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs) {
        return extractDataxArgs(dataxJobMeta, sysEnvs, null);
    }

    public static String[] extractDataxArgs(DataxJobMeta dataxJobMeta, Map<String, String> sysEnvs, String varRefOperator) {
        if (varRefOperator == null) {
            varRefOperator = RuntimeUtils.getEnvRefSymbol();
        }

        String jobScriptFile = dataxJobMeta.getJobScriptFile();
        List<String> invokeCmds = new ArrayList<>();
        invokeCmds.add(dataxJobMeta.getInvokePythonCmd());
        invokeCmds.add(dataxJobMeta.getDataxScript());
        invokeCmds.add(jobScriptFile);
        if (!sysEnvs.isEmpty()) {
            invokeCmds.add("-p");
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            boolean isFirst = true;

            for (Map.Entry<String, String> entry : sysEnvs.entrySet()) {

                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(" ");
                }

                sb.append("-D" + entry.getKey());
                sb.append("=");
                sb.append(varRefOperator);
                sb.append(entry.getValue());
                sb.append(varRefOperator);

            }

            sb.append("\"");
            invokeCmds.add(sb.toString());
        }

        String[] dataxArgs = invokeCmds.toArray(new String[invokeCmds.size()]);
        return dataxArgs;
    }


}
