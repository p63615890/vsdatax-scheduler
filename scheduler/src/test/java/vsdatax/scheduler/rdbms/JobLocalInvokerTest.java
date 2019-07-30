package vsdatax.scheduler.rdbms;

import vscommons.vsutils.io.PropertiesCfgUtils;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.EnvInitiator;
import vsdatax.scheduler.facade.JobInvokerByFileFacade;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.startup.SingleJobInvokeHelper;

import java.util.Map;
import java.util.Properties;

/**
 * @author JerryHuang
 */
public class JobLocalInvokerTest {
    public static void main(String[] args) {
        String jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_demo_num.job";
//        jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_demo_ssh.job";
                 jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_demo_onlytime.job";
//        jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_onlytime_brief.job";
//        jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_demo_only_sysvar.job";
//        jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_begin_end_both_in_db.job";
//         jobConfFile = "D:\\work\\projs\\incubator\\vsetl\\vsdatax\\scheduler\\vsdatax-scheduler\\dataxConf\\jobs\\job_demo_num_company.job";

        EnvInitiator.initEnv();
        JobInvokerByFileFacade.invoke(jobConfFile, null, 0);
    }


}
