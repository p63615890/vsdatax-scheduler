package vsdatax.scheduler.jobconf;

import java.util.Collection;

/**
 * @author JerryHuang
 * Create Time: 2019/6/4
 */
public interface IJobConfMgr {

    public void init(String confDir,String encoding);

    public Collection<JobConfModel> listJobConfs();

    public JobConfModel getJobConfModel(String jobId);

    public void removeJobConf(String jobId);

    public void updateJobConf(JobConfModel jobConfModel);

    public void addJobConf(JobConfModel jobConfModel);
    public void createOrUpdateJobConf (JobConfModel jobConfModel);



}
