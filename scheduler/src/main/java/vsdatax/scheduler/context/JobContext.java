package vsdatax.scheduler.context;


import vscommons.vsutils.ds.MapUtils;
import vscommons.vsutils.str.VsStringUtils;
import vsdatax.scheduler.env.AppCfgFactory;
import vsincr.conf.IncrEnvConf;
import vsincr.conf.JobStatusConf;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 封装作业配置元数据的各项配置项的类。
 *
 * @author JerryHuang
 * Create Time: 2019-07-08
 */
public class JobContext implements Serializable {


    private static final long serialVersionUID = -7656149849553773120L;
    /**
     * 存储Job配置里的所有原始配置项。
     */
    private Map<String, String> parameters;
    /**
     * 变量相关的配置项:parameters-dataxJobMeta=varConf
     */
    private IncrEnvConf incrEnvConf = new IncrEnvConf();

    private JobStatusConf jobStatusConf = new JobStatusConf();


    /**
     * 固定的基本作业配置项，从parameters里提取出来。
     */
    private DataxJobMeta dataxJobMeta;
    /**
     * 存储Job配置的最后修改时间
     */
    private long lastModified;


    public JobContext() {
        parameters = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public JobContext(Map<String, String> paramters) {
        this();
        this.putAll(paramters);
        dataxJobMeta = loadJobMeta();
        incrEnvConf.load(paramters);
        jobStatusConf.load(paramters);
        this.parameters.clear();
    }


    public DataxJobMeta getDataxJobMeta() {
        return dataxJobMeta;
    }

    public IncrEnvConf getIncrEnvConf() {
        return incrEnvConf;
    }

    public JobStatusConf getJobStatusConf() {
        return jobStatusConf;
    }

    /**
     * Removes all of the mappings from this map.
     */
    public void clear() {
        parameters.clear();
        incrEnvConf.clear();
        jobStatusConf.clear();
        this.dataxJobMeta = null;
    }


    /**
     * Associates all of the given map's keys and values in the JobContext.
     */
    private void putAll(Map<String, String> map) {
        parameters.putAll(map);
    }


    @Override
    public String toString() {
        return "{ parameters:" + parameters + " }";
    }

    private DataxJobMeta loadJobMeta() {
        Map<String, String> subMap = MapUtils.getSubProperties(parameters, "job.meta.base.");
        DataxJobMeta dataxJobMeta = new DataxJobMeta();

        dataxJobMeta.setJobId(subMap.get("jobid"));
//        if (subMap.containsKey("dataxJobId")) {
//            dataxJobMeta.setDataxJobId(subMap.get("dataxJobId"));
//        }
        dataxJobMeta.setJobGroupName(subMap.get("jobgroupname"));
        dataxJobMeta.setCronExpression(subMap.get("cronexpression"));


        //Load ssh configuration

        String sshHostName = subMap.get("ssh.host");
        String strPort = subMap.get("ssh.port");
        String sshUserName = subMap.get("ssh.userName");
        String sshPassword = subMap.get("ssh.password");
        String strSshTimeout = subMap.get("ssh.timeout");
        String sshOs = subMap.get("ssh.os");


        if (!VsStringUtils.isEmpty(sshHostName) && !VsStringUtils.isEmpty(sshUserName) && !VsStringUtils.isEmpty(sshPassword) && !VsStringUtils.isEmpty(sshOs)) {

            SSHHost sshHost = new SSHHost();

            int sshPort = 22;
            if (!VsStringUtils.isEmpty(strPort)) {
                sshPort = Integer.parseInt(strPort);
            }

            int sshTimeout = 1000;

            if (!VsStringUtils.isEmpty(strSshTimeout)) {
                sshTimeout = Integer.parseInt(strSshTimeout);
            }

            sshHost.setHost(sshHostName);
            sshHost.setPort(sshPort);
            sshHost.setUserName(sshUserName);
            sshHost.setPassword(sshPassword);
            sshHost.setTimeout(sshTimeout);
            sshHost.setOs(sshOs);
            dataxJobMeta.setSshHost(sshHost);
        }
        //The following steps populate default job meta configurations.
        String dataxHome = subMap.get("datax.home");
        if (VsStringUtils.isEmpty(dataxHome)) {
            dataxHome = AppCfgFactory.getCfg().getProperty("meta.conf.default.datax.home");
        }
        dataxJobMeta.setDataxHome(dataxHome);

        String envEncoding = subMap.get("env.encoding");
        if (VsStringUtils.isEmpty(envEncoding)) {
            envEncoding = AppCfgFactory.getCfg().getProperty("meta.conf.default.env.encoding");
        }
        dataxJobMeta.setEnvEncoding(envEncoding);

        String invokeWay = subMap.get("invoke.way");
        if (VsStringUtils.isEmpty(invokeWay)) {
            invokeWay = AppCfgFactory.getCfg().getProperty("meta.conf.default.invoke.way");
        }
        dataxJobMeta.setInvokeWay(invokeWay);
        String pythonCmd = subMap.get("invoke.python.cmd");
        if (VsStringUtils.isEmpty(pythonCmd)) {
            pythonCmd = AppCfgFactory.getCfg().getProperty("meta.conf.default.python.cmd");
        }
        dataxJobMeta.setInvokePythonCmd(pythonCmd);

        String dataxScriptFile = subMap.get("invoke.datax.script");
        if (VsStringUtils.isEmpty(dataxScriptFile)) {
            dataxScriptFile = makeDefaultDataxScriptFile(dataxHome);
        }
        dataxJobMeta.setDataxScript(dataxScriptFile);


        //////////////////////////////////////////////////////////////

        dataxJobMeta.setJobScriptFile(subMap.get("job.script"));

        dataxJobMeta.setCustomMeta(MapUtils.getSubProperties(parameters, "job.meta.custom."));
        return dataxJobMeta;
    }

    private static String makeDefaultDataxScriptFile(String dataxHome) {
        return dataxHome + "/bin/datax.py";
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}