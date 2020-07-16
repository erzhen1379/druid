package com.alibaba.druid.bvt.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallDenyStat;
import junit.framework.TestCase;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by wenshao on 21/01/2017.
 */
public class KeepAliveTest extends TestCase {
    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:x1");
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(20);
//        dataSource.setMinEvictableIdleTimeMillis(30000);
//        dataSource.setMaxEvictableIdleTimeMillis(30000);
        dataSource.setTimeBetweenEvictionRunsMillis(10);
        dataSource.setFilters("log4j");
        dataSource.setValidationQuery("select 1");

        Properties properties = new Properties();
        properties.put("druid.keepAlive", "true");
        dataSource.configFromPropety(properties);
    }

    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSource);
    }

    public void test_keepAlive() throws Exception {
        dataSource.init();

        for (int i = 0; i < 1000; ++i) {
            if (dataSource.getMinIdle() == dataSource.getPoolingCount()) {
                break;
            }
            Thread.sleep(10 * 1);
        }
        assertEquals(dataSource.getMinIdle(), dataSource.getPoolingCount());
        assertTrue(dataSource.isKeepAlive());

        Connection[] connections = new Connection[dataSource.getMaxActive()];
        for (int i = 0; i < connections.length; ++i) {
            connections[i] = dataSource.getConnection();
        }
        for (int i = 0; i < connections.length; ++i) {
            connections[i].close();
        }
        // assertEquals(dataSource.getMaxActive(), dataSource.getPoolingCount());



        // Thread.sleep(1000 * 1000);
    }
}
