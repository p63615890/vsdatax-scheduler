package vsdatax.scheduler.utils;

import org.junit.Test;
import vsdatax.scheduler.env.WorkEnvHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JerryHuang
 * Create Time:  2019/7/30
 */
public class PatternTest {
    @Test
    public void testExtractWorkEnv() {
        String input = "${vsapp.workdir}/jobs";
        Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = regex.matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void testExtractWorkEnv2() {
        String input = "${vsapp.workdir}/jobs";
        String regex="\\$\\{(vsapp.workdir)\\}";
        String result=input.replaceAll(regex, "f:/aaa");
        System.out.println("result:"+result);

    }
}
