package vsdatax.scheduler.context;

import java.io.Serializable;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class SSHHost implements Serializable {
    private String host;
    private int port;
    private String userName;
    private String password;
    /**
     * SSH 服务器端的操作系统。
     */
    private String os;
    private int timeout=1000;

    public SSHHost(String host, int port, String userName, String password, String os, int timeout) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.os = os;
        this.timeout = timeout;
    }

    public SSHHost() {

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
