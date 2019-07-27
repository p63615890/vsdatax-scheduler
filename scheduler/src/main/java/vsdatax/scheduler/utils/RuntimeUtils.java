package vsdatax.scheduler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取传给datax的环境变量引用符。
 *
 * @author JerryHuang
 * Create Time:  2019/7/25
 */
public class RuntimeUtils {
    private static Logger logger = LoggerFactory.getLogger(RuntimeUtils.class);

    /**
     * 获取传给datax的环境变量引用符。在windows 环境下是两个引号，在linux下是一个单引号。
     * //TODO:暂时不考虑 MAC或者其它操作系统。
     *
     * @return
     */
    public static String getEnvRefSymbol() {
        String osName =getOs();
        return getEnvRefSymbol(osName);
    }

    public static String getEnvRefSymbol(String osName) {

        logger.debug("Current Runtime OS:{}", osName);
        if (osName.startsWith("windows")) {
            return "\"\"";
        } else {
            return "\'";
        }
    }

    public static boolean isWindows(){
        return getOs().startsWith("windows");
    }
    private static String getOs(){
        return System.getProperty("os.name").toLowerCase();
    }

}
