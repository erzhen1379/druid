package com.alibaba.druid.bvt.utils;

import com.alibaba.druid.wall.WallDenyStat;
import org.junit.Assert;

import com.alibaba.druid.util.JdbcConstants;

import junit.framework.TestCase;

public class JdbcUtilsTest_for_odps extends TestCase {
    public void test_odps() throws Exception {
        Assert.assertEquals(JdbcConstants.ODPS_DRIVER, WallDenyStat.JdbcUtils.getDriverClassName("jdbc:odps:"));
    }
    public void test_odps_dbtype() throws Exception {
        Assert.assertEquals(JdbcConstants.ODPS, WallDenyStat.JdbcUtils.getDbType("jdbc:odps:", null));
    }
}
