package com.alibaba.druid.bvt.utils;

import com.alibaba.druid.wall.WallDenyStat;

import com.alibaba.druid.util.JdbcConstants;

import junit.framework.TestCase;


public class JdbcUtilsTest2 extends TestCase {
    public void test_get_0() throws Exception {
        assertEquals(JdbcConstants.ORACLE_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("JDBC:oracle:"));
    }

    public void test_gbase() throws Exception {
        assertEquals(JdbcConstants.GBASE_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("jdbc:gbase:"));
    }

    public void test_kingbase() throws Exception {
        assertEquals(JdbcConstants.KINGBASE_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("jdbc:kingbase:"));
    }

    public void test_xugu_dbtype() throws Exception {
        assertEquals(JdbcConstants.XUGU, WallDenyStat.JdbcUtils.getDbType("jdbc:xugu://127.0.0.1:5138/TEST", "com.xugu.cloudjdbc.Driver"));
    }

    public void test_xugu_driver() throws Exception {
        assertEquals(JdbcConstants.XUGU_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("jdbc:xugu:"));
    }

    public void test_kdb() throws Exception {
        assertEquals(JdbcConstants.KDB_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("jdbc:inspur:"));
    }
}
