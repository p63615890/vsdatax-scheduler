package vsdatax.scheduler.jobstatus;

import vsdatax.scheduler.env.DataSourceFactory;
import vsincr.jobstatus.JdbcJobStatusMgrSupport;

import javax.sql.DataSource;

/**
 * @author JerryHuang
 * Create Time:  2019/7/10
 */
public class JdbcJobStatusMgr extends JdbcJobStatusMgrSupport {

    @Override
    public DataSource getDataSource() {
        return DataSourceFactory.getDataSource();
    }
}
