package vsdatax.scheduler.erest.startup;

import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.exception.UnauthorizedException;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.route.Route;

import javax.xml.bind.DatatypeConverter;




public class HttpBasicAuthenticationPreprocessor implements Preprocessor {

    private String realm;
    private String[] skipFlags;

    public HttpBasicAuthenticationPreprocessor(String realm) {
        this(realm, (String[])null);
    }

    public HttpBasicAuthenticationPreprocessor(String realm, String... flags) {
        this.realm = realm;
        if(flags != null && flags.length > 0) {
            this.skipFlags = (String[])flags.clone();
        }

    }

    public void process(Request request) {
        Route route = request.getResolvedRoute();
        if(route == null || !route.isFlagged("not.secured") && !route.isFlagged("no.authentication") && !route.containsAnyFlags(this.skipFlags)) {
            String authorization = request.getHeader("Authorization");
            if(authorization == null || !authorization.startsWith("Basic ")) {
                this.throwUnauthorizedException();
            }

            String[] pieces = authorization.split(" ");
            byte[] bytes = DatatypeConverter.parseBase64Binary(pieces[1]);
            String credentials = new String(bytes, ContentType.CHARSET);
            String[] parts = credentials.split(":");
            if(parts.length < 2) {
                this.throwUnauthorizedException();
            }

            String inpUserName = parts[0];
            String inpPassword = parts[1];

            if(!this.checkAu(inpUserName, inpPassword)){
                this.throwUnauthorizedException();
            }

//            request.addHeader("X-AuthenticatedUser", parts[0]);
//            request.addHeader("X-AuthenticatedPassword", parts[1]);
        }
    }

    private void throwUnauthorizedException() {
        UnauthorizedException e = new UnauthorizedException("AUTH");//设置权限验证
        e.setHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
        throw e;
    }

    /**
     * TODO: not implemented yet
     *  简单加入安全验证
     */
    public boolean checkAu(String iun,String ipw){

        boolean re = false;

//        String username = ServerConfigFactory.getInstance().getProperty("server.username");
//        String password = ServerConfigFactory.getInstance().getProperty("server.password");


//        if(iun.equals(username)&&ipw.equals(password)){
//            re = true;
//        }
        return re;
    }
}