package vsdatax.scheduler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vsdatax.scheduler.context.JobContexHelper;
import vsdatax.scheduler.context.JobContext;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.job.DataxJobExecutor;
import vsdatax.scheduler.jobconf.IJobConfMgr;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsincr.jobstatus.IJobStatusMgr;
import vsincr.variable.DefaultIncrEnvGenerator;
import vsincr.variable.IIncrEnvGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Job调用服务。外部应用通过该服务直接调用服务
 * User: JerryHuang
 * Date: 2015/10/30
 * Time: 15:13
 */
public class JobExecuteService implements IJobExecuteService {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void executeByJobId(String jobId) {

        IJobConfMgr jobConfMgr = BcmFactory.getInstance().getBean("jobConfMgr", IJobConfMgr.class);
        IJobStatusMgr jobStatusMgr = BcmFactory.getInstance().getBean("jobStatusMgr", IJobStatusMgr.class);
        IIncrEnvGenerator incrEnvGenerator=new DefaultIncrEnvGenerator();
        JobConfModel jobConf = jobConfMgr.getJobConfModel(jobId);
        JobContext context = JobContexHelper.load(jobConf);
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("scheduledFireTime", new Date(System.currentTimeMillis()));
        // Log the time when the service is invoked
        logger.info("service :{} is fired at {}", JobExecuteService.class.getSimpleName(), new Date());   //记录任务开始的时间
        DataxJobExecutor.execute(incrEnvGenerator,jobStatusMgr, context, params);
    }

    @Override
    public void executeByJobConf(JobConfModel jobConf) {
        IJobStatusMgr jobStatusMgr = BcmFactory.getInstance().getBean("jobStatusMgr", IJobStatusMgr.class);
        IIncrEnvGenerator incrEnvGenerator=new DefaultIncrEnvGenerator();
        logger.info("service :{} is fired at {}", JobExecuteService.class.getSimpleName(), new Date());   //记录任务开始的时间
        Map<String, Object> params = new HashMap<String, Object>(1);
        JobContext context = JobContexHelper.load(jobConf);
        params.put("scheduledFireTime", new Date(System.currentTimeMillis()));
        DataxJobExecutor.execute(incrEnvGenerator, jobStatusMgr, context, params);
    }
}
