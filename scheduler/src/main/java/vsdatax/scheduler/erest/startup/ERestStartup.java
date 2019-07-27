package vsdatax.scheduler.erest.startup;

import org.restexpress.RestExpress;
import vsdatax.scheduler.env.EnvInitiator;
import vsdatax.scheduler.env.WorkEnvHelper;
import vsdatax.scheduler.startup.DataxJobBootstrap;

/**
 *
 * User: JerryHuang
 * Date: 2016/3/25
 * Time: 17:32
 */
public class ERestStartup {
    public static void main(String[] args) throws Exception {
        //Initiate the environments
        EnvInitiator.initEnv();
        Configuration config  = new Configuration();
        config.load(WorkEnvHelper.getDataxServerConfFile());



        DataxJobBootstrap.startup();
        String host = null;
        String strPort = null;

        int port = -1;
        if (args != null && args.length >= 2) {
            host = args[1];
            strPort = args[2];
            port = Integer.parseInt(strPort);
            config.setHost(host);
            config.setPort(port);
        }
        RestExpress server = new RestExpress()
                .setName( config.getServerName())
                .setBaseUrl("/");
        server.setHostname(config.getHost());
        server.setPort(config.getPort()).setExecutorThreadCount(config.getExecutorThreadCount()).setIoThreadCount(config.getWorkerCount());
//        String username = ServerConfigFactory.getInstance().getProperty("server.username");
//        String password = ServerConfigFactory.getInstance().getProperty("server.password");
//        server.addPreprocessor(new HttpBasicAuthenticationPreprocessor(username));
        Routes.define(config, server);


        mapExceptions(server);
        port = server.getPort();
        server.bind(port);
        server.awaitShutdown();
    }


    /**
     * @param server
     */
    private static void mapExceptions(RestExpress server) {
    }
}
