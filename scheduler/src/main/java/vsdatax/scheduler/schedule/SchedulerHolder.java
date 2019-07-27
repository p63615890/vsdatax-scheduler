package vsdatax.scheduler.schedule;

import org.quartz.Scheduler;

/**
 * User: JerryHuang
 * Date: 2015/11/12
 * Time: 15:37
 */
public class SchedulerHolder {
    private static Scheduler scheduler = null;

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static void setScheduler(Scheduler pScheduler) {
         scheduler = pScheduler;
    }
}
