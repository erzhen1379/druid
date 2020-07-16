package com.alibaba.druid.bvt.pool.property;

import com.alibaba.druid.PoolTestCase;
import com.alibaba.druid.wall.WallDenyStat;

import org.junit.Assert;

import com.alibaba.druid.pool.DruidDataSource;


public class PropertyTest_validationQuery extends PoolTestCase {
    private DruidDataSource dataSource;

    public void test_validationQuery() {
        System.setProperty("druid.validationQuery", "select 1");
        dataSource = new DruidDataSource();
        Assert.assertEquals("select 1", dataSource.getValidationQuery());
    }
    
    protected void tearDown() throws Exception {
        System.clearProperty("druid.validationQuery");
        WallDenyStat.JdbcUtils.close(dataSource);

        super.tearDown();
    }
}
