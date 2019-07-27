package vsdatax.scheduler.service;

import vsdatax.scheduler.jobconf.JobConfModel;

/**
 * 调用Job执行的服务接口
 * User: JerryHuang
 * Date: 2015/10/30
 * Time: 15:10
 */
public interface IJobExecuteService {

    void executeByJobId(String jobId);
    void executeByJobConf(JobConfModel jobConfModel);

}
