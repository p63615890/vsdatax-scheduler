package vsdatax.scheduler.erest.startup;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;

public abstract class Routes {

    public static final String root = "/vsdataxJob";

    public static final String uri_executeByJobId = root + "/executeByJobId";
    public static final String uri_executeByJobConf = root + "/executeByJobConf";
    public static final String uri_createJob = root + "/createJob";
    public static final String uri_updateJob = root + "/updateJob";
    public static final String uri_createOrUpdateJob = root + "/createOrUpdateJob";
    public static final String uri_removeJob = root + "/removeJob";

    public static final String uri_pauseJob = root + "/pauseJob";
    public static final String uri_resumeJob = root + "/resumeJob";
    public static final String uri_triggerJob = root + "/triggerJob";
    public static final String uri_getJobInfo = root + "/getJobInfo";

    public static void define(Configuration config, RestExpress server) {

        server.uri(uri_executeByJobId + "", config.getJobLifeCycleController())
                .action("executeByJobId", HttpMethod.GET) ;

        server.uri(uri_executeByJobConf + "", config.getJobLifeCycleController())
                .action("executeJobByJobMeta", HttpMethod.POST);

        server.uri(uri_createJob, config.getJobLifeCycleController())
                .action("createJob", HttpMethod.GET) ;

        server.uri(uri_updateJob, config.getJobLifeCycleController())
                .action("updateJob", HttpMethod.POST) ;

        server.uri(uri_createOrUpdateJob, config.getJobLifeCycleController())
                .action("createOrUpdateJob", HttpMethod.POST);

        server.uri(uri_removeJob, config.getJobLifeCycleController())
                .action("removeJob", HttpMethod.GET) ;



        server.uri(uri_pauseJob, config.getJobLifeCycleController())
                .action("pauseJob", HttpMethod.GET);

        server.uri(uri_resumeJob, config.getJobLifeCycleController())
                .action("resumeJob", HttpMethod.GET) ;



        server.uri(uri_triggerJob, config.getJobLifeCycleController())
                .action("triggerJob", HttpMethod.GET);

        server.uri(uri_getJobInfo, config.getJobLifeCycleController())
                .action("getJobInfo", HttpMethod.GET) ;


    }
}
