package com.alibaba.druid.bvt.filter;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.druid.wall.WallDenyStat;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.stat.JdbcSqlStat;

public class StatFilterOpenInputStreamCountTest extends TestCase {

    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:mock:xxx");
        dataSource.setFilters("stat");
        dataSource.setTestOnBorrow(false);
        dataSource.getProxyFilters().add(new FilterAdapter() {

            @Override
            public java.io.InputStream resultSet_getBinaryStream(FilterChain chain, ResultSetProxy result, int columnIndex)
                                                                                                                           throws SQLException {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public java.io.InputStream resultSet_getBinaryStream(FilterChain chain, ResultSetProxy result, String columnLabel)
                                                                                                                              throws SQLException {
                return new ByteArrayInputStream(new byte[0]);
            }
        });
        dataSource.init();
    }

    protected void tearDown() throws Exception {
        WallDenyStat.JdbcUtils.close(dataSource);
    }

    public void test_stat() throws Exception {
        Connection conn = dataSource.getConnection();

        String sql = "select 'x'";
        PreparedStatement stmt = conn.prepareStatement("select 'x'");

        JdbcSqlStat sqlStat = dataSource.getDataSourceStat().getSqlStat(sql);

        Assert.assertEquals(0, sqlStat.getInputStreamOpenCount());

        ResultSet rs = stmt.executeQuery();
        rs.next();
        rs.getBinaryStream(1);
        rs.getBinaryStream(2);
        rs.close();
        stmt.close();

        conn.close();

        Assert.assertEquals(2, sqlStat.getInputStreamOpenCount());

        sqlStat.reset();
        Assert.assertEquals(0, sqlStat.getInputStreamOpenCount());
    }

    public void test_stat_1() throws Exception {
        Connection conn = dataSource.getConnection();

        String sql = "select 'x'";
        PreparedStatement stmt = conn.prepareStatement("select 'x'");

        JdbcSqlStat sqlStat = dataSource.getDataSourceStat().getSqlStat(sql);

        Assert.assertEquals(0, sqlStat.getInputStreamOpenCount());

        ResultSet rs = stmt.executeQuery();
        rs.next();
        rs.getBinaryStream("1");
        rs.getBinaryStream("2");
        rs.getBinaryStream("3");
        rs.close();
        stmt.close();

        conn.close();

        Assert.assertEquals(3, sqlStat.getInputStreamOpenCount());

        sqlStat.reset();
        Assert.assertEquals(0, sqlStat.getInputStreamOpenCount());
    }
}
