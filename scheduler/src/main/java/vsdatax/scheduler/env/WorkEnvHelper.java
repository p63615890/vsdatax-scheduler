package vsdatax.scheduler.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.io.IOUtils;

/**
 * User: JerryHuang
 * Date: 2015/8/5
 * Time: 15:52
 */
public class WorkEnvHelper {
    private static Logger logger = LoggerFactory.getLogger(WorkEnvHelper.class);


    private static String rootPath = null;

    public static void init(String pRootPath) {
        rootPath = pRootPath;
    }

    public static String getRootPath() {

        return rootPath;
    }

    public static String getJobConfDir() {
        return IOUtils.composeFile(rootPath, "jobs");
    }

    public static String getQuartzFile() {
        return IOUtils.composeFile(rootPath, "quartz.properties");
    }

    public static String getBcmDir() {
        return IOUtils.composeFile(rootPath, "beanDef");
    }

    public static String getLogCfgFile() {
        return IOUtils.composeFile(rootPath, "log4j.properties");
    }

    public static String getDataxServerConfFile() {
        return IOUtils.composeFile(rootPath, "vsdatax-server.properties");
    }
    public static String getSysVarRegisterFile() {
        return IOUtils.composeFile(rootPath, "sysvar.register.properties");
    }

    public static String getSchedulerJdbcConfFile(){
          return IOUtils.composeFile(rootPath, "scheduler_jdbc.properties");
    }
      public static String getAppConf(){
          return IOUtils.composeFile(rootPath, "appCfg.properties");
    }


}
