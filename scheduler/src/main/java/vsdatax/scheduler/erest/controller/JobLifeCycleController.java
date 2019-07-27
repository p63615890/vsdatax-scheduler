package vsdatax.scheduler.erest.controller;

import com.google.gson.Gson;
import org.restexpress.Request;
import org.restexpress.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vsdatax.scheduler.job.JobInfo;
import vsdatax.scheduler.jobconf.JobConfModel;
import vsdatax.scheduler.service.IJobExecuteService;
import vsdatax.scheduler.service.IJobLifeCycleService;

import java.io.IOException;
import java.util.List;

/***
 * TODO :需要对服务进行更严谨的规划和处理。
 */
public class JobLifeCycleController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String JOB_ID = "jobId";
    private static final String JOB_GROUPNAME = "jobGroupName";
    private IJobLifeCycleService lifeCycleService;
    private IJobExecuteService executeService;
    private static final Gson gson = new Gson();

    public JobLifeCycleController(IJobLifeCycleService lifeCycleService, IJobExecuteService executeService) {
        super();
        this.lifeCycleService = lifeCycleService;
        this.executeService = executeService;
    }


    public String executeByJobId(Request request, Response response) {
        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        executeService.executeByJobId(jobId);
        System.out.println("=======================================");
        return "SUCCESS";
    }

    public String executeJobByJobMeta(Request request, Response response) {
        JobConfModel jobConf = request.getBodyAs(JobConfModel.class);
        executeService.executeByJobConf(jobConf);
        return "SUCCESS";
    }


    public String createJob(Request request, Response response) throws IOException {
        JobConfModel jobMeta = request.getBodyAs(JobConfModel.class);
        if (jobMeta != null) {
            logger.debug("addJdbcJob result:{}", jobMeta);
            lifeCycleService.createJob(jobMeta);
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }

    public String updateJob(Request request, Response response) {
        JobConfModel jobMeta = request.getBodyAs(JobConfModel.class);
        if (jobMeta != null) {
            lifeCycleService.updateJob(jobMeta);
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }

    public String createOrUpdateJob(Request request, Response response) {

        JobConfModel jobMeta = request.getBodyAs(JobConfModel.class);
        if (jobMeta != null) {
            lifeCycleService.createOrUpdateJob(jobMeta);
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }


    public String removeJob(Request request, Response response) {
        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        String jobGroupName = request.getHeader(JOB_GROUPNAME, "No jobGroupName provided");
        logger.debug("removeJob jobId:{},jobGroupName:{}", jobId, jobGroupName);
        lifeCycleService.removeJob(jobId, jobGroupName);
        return "SUCCESS";
    }

    public String listJobs(Request request, Response response) {
        List<JobInfo> allListJob = lifeCycleService.listJob();
        //list转化为jsonArray
        return gson.toJson(allListJob);
    }

    public String getJobInfo(Request request, Response response) {
        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        String jobGroupName = request.getHeader(JOB_GROUPNAME, "No jobGroupName provided");
        logger.debug("Get jobId:{},jobGroupName:{}", jobId, jobGroupName);
        JobInfo jobDetail = lifeCycleService.getJobInfo(jobGroupName, jobId);
        //list转化为jsonArray
        return gson.toJson(jobDetail);
    }


    public String pauseJob(Request request, Response response) {

        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        String jobGroupName = request.getHeader(JOB_GROUPNAME, "No jobGroupName provided");
        boolean result = lifeCycleService.pauseJob(jobId, jobGroupName);
        return gson.toJson(result);
    }

    public String resumeJob(Request request, Response response) {
        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        String jobGroupName = request.getHeader(JOB_GROUPNAME, "No jobGroupName provided");
        boolean result = lifeCycleService.resumeJob(jobId, jobGroupName);
        return gson.toJson(result);
    }

    public String triggerJob(Request request, Response response) {

        String jobId = request.getHeader(JOB_ID, "No jobId provided");
        String jobGroupName = request.getHeader(JOB_GROUPNAME, "No jobGroupName provided");
        boolean result = lifeCycleService.triggerJob(jobId, jobGroupName);
        return gson.toJson(result);
    }

}
