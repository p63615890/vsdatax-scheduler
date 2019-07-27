package vsdatax.scheduler.context;


import vsdatax.scheduler.job.JobConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 作业定义的基本元数据类。
 * 由于不同数据源在定义作业元数据的时候，会存在不同的配置项，因此这里抽象出了公共的基础配置项和可变的配置项。
 *
 * @author JerryHuang
 */
public class DataxJobMeta implements Serializable {
    private static final long serialVersionUID = -1110869144075268549L;

    public final static long DEFAULT_PROCESS_SLEEP_MS = 1000;


    /**
     * 作业 id：区分作业的唯一性标识
     */
    private String jobId;

    private String envEncoding="utf-8";
 

    private SSHHost sshHost=null;
    /**
     * Datax程序的根路径
     */
    private String dataxHome;
    /**
     * Datax提供的运行job的启动脚本。通常是$DATA_HOME/bin/datax.py。
     */
    private String dataxScript;


    /**
     * 包含作业定义的datax job脚本文件
     */
    private String jobScriptFile;

    /**
     * TODO:Deprecated .Remove it!
     * Job进程休眠时间
     */
    private long jobProcessSleepMs = DEFAULT_PROCESS_SLEEP_MS;
    /**
     * Job的调用方式，有两种类型：进程方式和线程方式
     */
    private String invokeWay = JobConstants.INVOKE_WAY_PROCESS;
    /**
     * 启动作业的 python命令名。根据作业的宿主机的操作系统进行配置，例如python、python2、python3等
     */
    private String invokePythonCmd = "python";


    // What hell is this id for in datax???
//    private String dataxJobId = "-1";
    /**
     * 其它自定义的元数据配置项
     */
    private Map<String, String> customMeta = new HashMap<>();

    /**
     * //////////////////////////////////////////////////////////////////////////////////////
     * 以下定义和任务调度（Quartz)相关。对于采用外部任务调用的应用，这些配置信息可以不用。
     */
    /**
     * 作业组名
     */
    private String jobGroupName = null;

    /**
     * 作业的定时任务的cron表达式
     */
    private String cronExpression;
    /////////////////////////////////////////////////////////////////////////////

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    public String getJobScriptFile() {
        return jobScriptFile;
    }

    public void setJobScriptFile(String jobScriptFile) {
        this.jobScriptFile = jobScriptFile;
    }



    public void putParam(String key, String val) {
        this.customMeta.put(key, val);
    }

    public String getParam(String key) {
        return this.customMeta.get(key);
    }

    public String getDataxHome() {
        return dataxHome;
    }

    public void setDataxHome(String dataxHome) {
        this.dataxHome = dataxHome;
    }

    public long getJobProcessSleepMs() {
        return jobProcessSleepMs;
    }

    public void setJobProcessSleepMs(long jobProcessSleepMs) {
        this.jobProcessSleepMs = jobProcessSleepMs;
    }

    public String getInvokeWay() {
        return invokeWay;
    }

    public void setInvokeWay(String invokeWay) {
        this.invokeWay = invokeWay;
    }

    public String getInvokePythonCmd() {
        return invokePythonCmd;
    }

    public void setInvokePythonCmd(String invokePythonCmd) {
        this.invokePythonCmd = invokePythonCmd;
    }

    public String getDataxScript() {
        return dataxScript;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setDataxScript(String dataxScript) {
        this.dataxScript = dataxScript;
    }


    public Map<String, String> getCustomMeta() {
        return customMeta;
    }

    public void setCustomMeta(Map<String, String> customMeta) {
        this.customMeta = customMeta;
    }






    public SSHHost getSshHost() {
        return sshHost;
    }

    public void setSshHost(SSHHost sshHost) {
        this.sshHost = sshHost;
    }

    public String getEnvEncoding() {
        return envEncoding;
    }

    public void setEnvEncoding(String envEncoding) {
        this.envEncoding = envEncoding;
    }


}
