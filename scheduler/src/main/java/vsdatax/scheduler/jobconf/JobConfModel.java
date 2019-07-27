package vsdatax.scheduler.jobconf;

import java.io.Serializable;
import java.util.Map;

/**
 * 存储运行一个Datax job所需要的所有相关配置项。可以理解为里面的配置项对应于一个*.job的配置文件。
 * @author JerryHuang
 * Create Time: 2019/6/4
 */
public class JobConfModel implements Serializable {
    private long lastModified;
    private Map<String,String> jobConfParams;


    public void load(Map<String,String> jobConfParams,long lastModified){
        this.jobConfParams=jobConfParams;
        this.lastModified=lastModified;

    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getJobId(){
        return jobConfParams.get(JobConfKey.KEY_JOBID);
    }

    public Map<String, String> getJobConfParams() {
        return jobConfParams;
    }

    public void setJobConfParams(Map<String, String> jobConfParams) {
        this.jobConfParams = jobConfParams;
    }

}
