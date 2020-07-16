package com.alibaba.druid.bvt.filter.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.alibaba.druid.wall.WallDenyStat;
import junit.framework.TestCase;

import com.alibaba.druid.pool.DruidDataSource;

public class LogFilterTest2 extends TestCase {

    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:derby:classpath:petstore-db");
        dataSource.setFilters("log4j");
    }
    
    public void test_select() throws Exception {
        Connection conn = dataSource.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ITEM WHERE LISTPRICE > 10");
        
        for (int i = 0; i < 10; ++i) {
            ResultSet rs = stmt.executeQuery();
            rs.close();
        }
        
        stmt.close();
        
        conn.close();
    }

    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSource);
    }
}
