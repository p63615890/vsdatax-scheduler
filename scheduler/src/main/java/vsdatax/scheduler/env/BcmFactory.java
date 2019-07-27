package vsdatax.scheduler.env;

import vscommons.bcm.BeanContext;

/**
 * User: JerryHuang
 * Date: 2016/7/7
 * Time: 14:45
 */
public class BcmFactory {
    private static BeanContext beanContext=null;

    public static void init(String beanCfgDir) {
        beanContext=new BeanContext();
        beanContext.init(beanCfgDir);
    }

    public static BeanContext getInstance(){
        return beanContext;
    }
}
