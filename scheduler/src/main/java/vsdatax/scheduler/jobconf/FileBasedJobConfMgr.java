package vsdatax.scheduler.jobconf;

import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import vscommons.vsutils.io.IOUtils;
import vscommons.vsutils.io.PropertiesCfgUtils;
import vscommons.vsutils.str.VsStringUtils;
import vsdatax.scheduler.env.AppCfgFactory;
import vsdatax.scheduler.env.EnvConstants;
import vsdatax.scheduler.env.WorkEnvHelper;
import vsdatax.scheduler.job.JobConstants;
import vsincr.utils.EnvUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于文件存储的Job的配置管理程序。
 * 注：为方便快速匹配，Job配置文件名必须是$jobstatus.meta.base.jobId的值，加上".job"的后缀。
 *
 * @author JerryHuang
 * Create Time: 2019/6/4
 */
public class FileBasedJobConfMgr implements IJobConfMgr {
    private String confDir = null;
    private String encoding = null;
    private Map<String, JobConfModel> jobConfs = new LinkedTreeMap<>();


    @Override
    public void init() {
        encoding = EnvUtils.getFileEncoding();
        confDir = initConfDir();
        Collection<File> confFiles = FileUtils.listFiles(new File(this.confDir), FileFilterUtils.suffixFileFilter(JobConstants.JOBCONF_FILE_SUFFIX), null);

        Map<String, String> jobConf = null;
        JobConfModel jobConfModel = null;
        String jobId = null;
        long lastModified = 0;
        for (File file : confFiles) {
            jobConf = extractJobConf(file);
            jobId = jobConf.get(JobConfKey.KEY_JOBID);
            lastModified = file.lastModified();
            jobConfModel = new JobConfModel();
            jobConfModel.setJobConfParams(jobConf);
            jobConfModel.setLastModified(lastModified);
            jobConfs.put(jobId, jobConfModel);
        }
    }

    private String initConfDir() {
        String confDir = AppCfgFactory.getCfg().getProperty("incr.job.conf.dir");
        if (VsStringUtils.isEmpty(confDir)) {
            //Use default configuration
            confDir = IOUtils.composeFile(WorkEnvHelper.getRootPath(), "jobs");
        } else {
            String regex="\\$\\{("+EnvConstants.ENV_NAME_WORKDIR+")\\}";
            confDir=confDir.replaceAll(regex, WorkEnvHelper.getRootPath());

            if (VsStringUtils.isEmpty(confDir)) {
                throw new JobConfException("The value of [incr.job.conf.dir] should be configurated correctly!");
            }
        }

        return confDir;
    }

    private Map<String, String> extractJobConf(File confFile) {
        Properties properties = PropertiesCfgUtils.loadProps(confFile.getAbsolutePath());
        return PropertiesCfgUtils.prop2Map(properties);
    }


    @Override
    public Collection<JobConfModel> listJobConfs() {
        return jobConfs.values();
    }

    @Override
    public JobConfModel getJobConfModel(String jobId) {
        return jobConfs.get(jobId);
    }

    @Override
    public void removeJobConf(String jobId) {
        try {
            FileUtils.forceDelete(new File(makeJobConfFileName(jobId)));
        } catch (IOException e) {
            JobConfException exception = new JobConfException(e);
            exception.setFailCode(JobConfException.CODE_FAIL_DELETED);
            throw exception;
        }
    }

    @Override
    public void updateJobConf(JobConfModel jobConfModel) {
        //Use create or update time  of job files as lastModified time
        createOrUpdateJobConf(jobConfModel);
    }

    @Override
    public void addJobConf(JobConfModel jobConfModel) {
        //Use File create or update time as lastModified time,so do nothing to handle file time
        createOrUpdateJobConf(jobConfModel);
    }

    @Override
    public void createOrUpdateJobConf(JobConfModel jobConfModel) {
        File confFile = new File(makeJobConfFileName(jobConfModel.getJobId()));
        try {
            FileUtils.write(confFile, map2PropString(jobConfModel.getJobConfParams()), this.encoding, false);
        } catch (IOException e) {
            JobConfException exception = new JobConfException(e);
            exception.setFailCode(JobConfException.CODE_FAIL_UPDATE);
            throw exception;
        }
    }

    private String makeJobConfFileName(String jobId) {
        return IOUtils.composeFile(confDir, jobId + ".job");
    }


    private String map2PropString(Map<String, String> jobConfParams) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : jobConfParams.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(EnvUtils.getLineSeperator());
            }
            sb.append(key);
            sb.append("=");
            sb.append(val);
            return sb.toString();

        }
        return sb.toString();
    }

}
