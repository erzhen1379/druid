package com.alibaba.druid.bvt.pool;

import java.sql.Connection;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.druid.wall.WallDenyStat;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.pool.DruidDataSource;

public class LockFairTest extends TestCase {

    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:xx");
    }

    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSource);
    }

    public void test_fair() throws Exception {
        Assert.assertEquals(false, ((ReentrantLock) dataSource.getLock()).isFair());
        dataSource.setMaxWait(100);

        Assert.assertEquals(true, ((ReentrantLock) dataSource.getLock()).isFair());
        {
            Connection conn = dataSource.getConnection();
            conn.close();
        }
        dataSource.setMaxWait(110);

        Assert.assertEquals(true, ((ReentrantLock) dataSource.getLock()).isFair());
        {
            Connection conn = dataSource.getConnection();
            conn.close();
        }
        
        dataSource.setMaxWait(0);

        Assert.assertEquals(true, ((ReentrantLock) dataSource.getLock()).isFair());
        {
            Connection conn = dataSource.getConnection();
            conn.close();
        }
    }

    public void test_fair_1() throws Exception {

        Connection conn = dataSource.getConnection();
        conn.close();

        Assert.assertEquals(false, ((ReentrantLock) dataSource.getLock()).isFair());
        dataSource.setMaxWait(100);

        Assert.assertEquals(false, ((ReentrantLock) dataSource.getLock()).isFair());
    }
}
