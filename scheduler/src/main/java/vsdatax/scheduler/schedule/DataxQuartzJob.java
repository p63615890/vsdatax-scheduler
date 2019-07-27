package vsdatax.scheduler.schedule;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.time.TimeUtils;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.startup.SingleJobInvokeHelper;
import vsincr.jobstatus.IJobStatusMgr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DataxQuartzJob implements Job {
    private Logger logger = LoggerFactory.getLogger(DataxQuartzJob.class);

    public void execute(JobExecutionContext context) {
        JobContext jobContext = (JobContext) context.getMergedJobDataMap().get("jobContext");

        // Every jobstatus has its own jobstatus detail
        JobDetail jobDetail = context.getJobDetail();

        // The name is defined in the jobstatus definition
        String jobName = ((JobDetailImpl) jobDetail).getName();

        // Log the time the jobstatus started
        logger.info("=======================Job [{}] is fired at [{}]", jobName, TimeUtils.formatDate(new Date(), TimeUtils.DATE_FORMAT_DATE_CHINESE));   //记录任务开始的时间
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("scheduledFireTime", TimeUtils.formatDate(context.getScheduledFireTime(), TimeUtils.DATE_FORMAT_DATE_TIME));
        IJobStatusMgr jobStatusMgr  = BcmFactory.getInstance().getBean("jobStatusMgr",IJobStatusMgr.class);
        SingleJobInvokeHelper.invoke(jobStatusMgr,jobContext, params);
    }
}
