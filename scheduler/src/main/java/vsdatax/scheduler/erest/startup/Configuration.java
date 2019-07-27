package vsdatax.scheduler.erest.startup;

/**
 * @author JerryHuang
 * Create Time: 2019/6/8
 */


import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.common.exception.ConfigurationException;
import org.restexpress.util.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vscommons.vsutils.exception.StackTraceUtils;
import vsdatax.scheduler.env.BcmFactory;
import vsdatax.scheduler.erest.controller.JobLifeCycleController;
import vsdatax.scheduler.service.IJobExecuteService;
import vsdatax.scheduler.service.IJobLifeCycleService;
import vsdatax.scheduler.startup.DataxJobBootstrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;


public class Configuration
        extends Environment {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String NAME_PROPERTY = "serverName";
    private static final String PORT_PROPERTY = "port";
    private static final String HOST_PROPERTY = "host";
    private static final String DEFAULT_FORMAT_PROPERTY = "defaultFormat";
    private static final String WORKER_COUNT_PROPERTY = "workerCount";
    private static final String EXECUTOR_THREAD_COUNT_PROPERTY = "executorThreadCount";
    private static final String DEFAULT_HOST = "0.0.0.0";

    private static final int DEFAULT_WORKER_COUNT = 0;
    private static final int DEFAULT_EXECUTOR_THREAD_COUNT = 0;
    private String host;
    private int port;
    private String serverName;
    private String defaultFormat;
    private int workerCount;
    private int executorThreadCount;
    private JobLifeCycleController jobLifeCycleController=null;

    @Override
    protected void load(String filename) throws ConfigurationException, FileNotFoundException, IOException {
        super.load(filename);
    }

    @Override
    protected void fillValues(Properties p) {
        this.serverName = p.getProperty(NAME_PROPERTY, RestExpress.DEFAULT_NAME);
        this.host = p.getProperty(HOST_PROPERTY, DEFAULT_HOST);
        this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
        this.defaultFormat = p.getProperty(DEFAULT_FORMAT_PROPERTY, Format.JSON);
        this.workerCount = Integer.parseInt(p.getProperty(WORKER_COUNT_PROPERTY, String.valueOf(DEFAULT_WORKER_COUNT)));
        this.executorThreadCount = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_COUNT_PROPERTY, String.valueOf(DEFAULT_EXECUTOR_THREAD_COUNT)));
        try {
            initialize();
        } catch (UnknownHostException e) {
            logger.error(StackTraceUtils.getStackTrace(e));
        }
    }

    private void initialize() throws UnknownHostException {
        IJobLifeCycleService jobLifeCycleService= BcmFactory.getInstance().getBean("jobLifeCycleService",IJobLifeCycleService.class);
        IJobExecuteService executeService= BcmFactory.getInstance().getBean("jobExecuteService",IJobExecuteService.class);;
        jobLifeCycleController=new JobLifeCycleController(jobLifeCycleService,executeService);
    }
    public String getDefaultFormat() {
        return defaultFormat;
    }

    public int getPort() {
        return port;
    }

    public String getServerName() {
        return serverName;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public int getExecutorThreadCount() {
        return executorThreadCount;
    }

     JobLifeCycleController getJobLifeCycleController() {
        return jobLifeCycleController;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
