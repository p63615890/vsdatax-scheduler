package vsdatax.scheduler.utils;

import com.jcraft.jsch.Session;
import org.junit.Test;
import vsdatax.scheduler.context.SSHHost;

/**
 * @author JerryHuang
 * Create Time:  2019/7/17
 */
public class SSHTest {
    @Test
    public   void testExec() {
        Session session = null;
        try {
            SSHHost sshHost=new SSHHost("master", 22, "root", "xxxxx","linux", 1000);

           String result= SSHUtils.executeOnce(sshHost,"/test.sh","utf-8");
           System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
