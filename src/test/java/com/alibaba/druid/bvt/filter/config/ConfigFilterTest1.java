package com.alibaba.druid.bvt.filter.config;

import com.alibaba.druid.wall.WallDenyStat;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

public class ConfigFilterTest1 extends TestCase {
    private DruidDataSource dataSource;
    
    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:xxx");
        dataSource.setFilters("config");
    }
    
    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSource);
    }
    
    public void test_decrypt() throws Exception {
        String plainPassword = "abcdefg1234567890";
        
        dataSource.setPassword(ConfigTools.encrypt(plainPassword));
        
        Assert.assertFalse(plainPassword.equals(dataSource.getPassword()));
        
        dataSource.addConnectionProperty(ConfigFilter.CONFIG_DECRYPT, "true");
        
        dataSource.init();
        
        Assert.assertEquals(plainPassword, dataSource.getPassword());
    }
    
}
