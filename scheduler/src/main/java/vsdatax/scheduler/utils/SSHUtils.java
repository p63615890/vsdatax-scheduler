package vsdatax.scheduler.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;
import vsdatax.scheduler.context.SSHHost;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class SSHUtils {

    private static Session getJSchSession(SSHHost sshHost) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshHost.getUserName(), sshHost.getHost(), sshHost.getPort());
        session.setPassword(sshHost.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(sshHost.getTimeout());
        session.connect();

        return session;
    }

    private static String executeCmd(Session session, String command, String resultEncoding) throws IOException, JSchException {
        ChannelExec channelExec = null;
        String result = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            result = IOUtils.toString(in, resultEncoding);
        } finally {
            if (channelExec != null)
                channelExec.disconnect();
        }
        return result;

    }

    public static String executeOnce(SSHHost sshHost, String command, String resultEncoding) throws IOException, JSchException {

        String result = null;
        Session session = null;
        try {
            session = getJSchSession(sshHost);
            result = executeCmd(session, command, resultEncoding);

        } finally {
            close(session);
        }
        return result;

    }

    private static void close(Session session) {
        if (session != null) {
            session.disconnect();
        }
    }

}
