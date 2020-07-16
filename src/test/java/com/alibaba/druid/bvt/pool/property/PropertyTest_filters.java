package com.alibaba.druid.bvt.pool.property;

import com.alibaba.druid.PoolTestCase;
import com.alibaba.druid.wall.WallDenyStat;

import org.junit.Assert;

import com.alibaba.druid.pool.DruidDataSource;


public class PropertyTest_filters extends PoolTestCase {
    private DruidDataSource dataSource;

    public void test_stat() {
        System.setProperty("druid.filters", "stat");
        dataSource = new DruidDataSource();
        Assert.assertEquals(1, dataSource.getProxyFilters().size());
        Assert.assertEquals("com.alibaba.druid.filter.stat.StatFilter", dataSource.getFilterClassNames().get(0));
    }
    
    public void test_false() {
        System.setProperty("druid.filters", "");
        dataSource = new DruidDataSource();
        Assert.assertEquals(0, dataSource.getProxyFilters().size());
        
        Assert.assertNull(dataSource.getWallStatMap());
    }
    
    protected void tearDown() throws Exception {
        System.clearProperty("druid.filters");
        WallDenyStat.JdbcUtils.close(dataSource);

        super.tearDown();
    }
}
