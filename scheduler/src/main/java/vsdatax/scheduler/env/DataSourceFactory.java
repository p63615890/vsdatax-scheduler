package vsdatax.scheduler.env;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import vscommons.vsutils.io.PropertiesCfgUtils;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author JerryHuang
 * Create Time:  2019/7/10
 */
public class DataSourceFactory {

    private static DataSource ds = null;

    public static void init(String jdbcConf) {
        Properties properties = PropertiesCfgUtils.loadProps(jdbcConf);

        try {
            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }
}
