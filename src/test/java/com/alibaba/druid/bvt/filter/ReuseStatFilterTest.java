package com.alibaba.druid.bvt.filter;

import com.alibaba.druid.wall.WallDenyStat;
import junit.framework.TestCase;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;

public class ReuseStatFilterTest extends TestCase {

    private DruidDataSource dataSourceA;
    private DruidDataSource dataSourceB;

    protected void setUp() throws Exception {
        dataSourceA = new DruidDataSource();
        dataSourceB = new DruidDataSource();

        dataSourceA.setUrl("jdbc:mock:xxx_A");
        dataSourceB.setUrl("jdbc:mock:xxx_B");
        
        StatFilter filter = new StatFilter();
        
        dataSourceA.getProxyFilters().add(filter);
        dataSourceB.getProxyFilters().add(filter);

        dataSourceA.init();
        dataSourceB.init();
    }
    
    public void test_execute() throws Exception {
        
    }

    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSourceA);
        WallDenyStat.JdbcUtils.close(dataSourceB);
    }
}
