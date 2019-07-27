package vsdatax.scheduler.env;

import javafx.print.PrinterJob;
import org.apache.log4j.PropertyConfigurator;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.schedule.DataxJobManager;
import vsdatax.scheduler.schedule.IJobManager;
import vsdatax.scheduler.service.IJobLifeCycleService;
import vsdatax.scheduler.service.NonTransJobLifeCycleService;
import vsincr.jobstatus.IJobStatusMgr;
import vsincr.sysvar.SystemVarMgr;
import vsincr.utils.EnvUtils;


/**
 * 初始化程序运行所需的环境，主要是各类配置的初始化。
 *
 * @author JerryHuang
 * Create Time: 2019/6/4
 */
public class EnvInitiator {

    public static void initEnv() {
        String workRoot = System.getProperty("vsapp.workdir");
        WorkEnvHelper.init(workRoot);
        PropertyConfigurator.configure(WorkEnvHelper.getLogCfgFile());


        AppCfgFactory.init(WorkEnvHelper.getAppConf());

        if (AppCfgFactory.getCfg().getBoolean("scheduler.jdbc.enable")) {
            DataSourceFactory.init(WorkEnvHelper.getSchedulerJdbcConfFile());
        }
        BcmFactory.init(WorkEnvHelper.getBcmDir());


        SystemVarMgr.init(WorkEnvHelper.getSysVarRegisterFile());


        IJobConfMgr jobConfMgr = BcmFactory.getInstance().getBean("jobConfMgr", IJobConfMgr.class);
        jobConfMgr.init(WorkEnvHelper.getJobConfDir(), EnvUtils.getFileEncoding());
        IJobLifeCycleService jobLifeCycleService=  BcmFactory.getInstance().getBean("jobLifeCycleService",IJobLifeCycleService.class);
        IJobStatusMgr jobStatusMgr=BcmFactory.getInstance().getBean("jobStatusMgr",IJobStatusMgr.class);
        IJobManager jobManager= BcmFactory.getInstance().getBean("jobManager",IJobManager.class);
        jobLifeCycleService.init(jobConfMgr,jobStatusMgr,jobManager);
    }
}
