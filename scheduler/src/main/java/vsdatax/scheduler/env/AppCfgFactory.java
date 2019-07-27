package vsdatax.scheduler.env;

import vscommons.vsutils.config.ResourceConfig;

/**
 * @author JerryHuang
 * Create Time:  2019/7/10
 */
public class AppCfgFactory {
    static ResourceConfig resourceConfig= null;
    public static void init(String appCfg){
        resourceConfig=new ResourceConfig(appCfg);
    }

    public static ResourceConfig getCfg(){
        return resourceConfig;
    }
}
