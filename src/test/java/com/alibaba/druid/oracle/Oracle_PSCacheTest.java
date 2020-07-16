package com.alibaba.druid.oracle;

import com.alibaba.druid.DbTestCase;
import com.alibaba.druid.wall.WallDenyStat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by wenshao on 23/07/2017.
 */
public class Oracle_PSCacheTest extends DbTestCase {
    public Oracle_PSCacheTest() {
        super("pool_config/oracle_db_sonar.properties");
    }

    public void test_oracle() throws Exception {
        for (int i = 0; i < 1000; ++i) {
            System.out.println(i + " : -----------------------------");
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection();
                pstmt = conn.prepareStatement("select * from t1 where f0 = ?");
                pstmt.setString(1, "3");
                rs = pstmt.executeQuery();
                WallDenyStat.JdbcUtils.printResultSet(rs);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                 WallDenyStat.JdbcUtils.close(rs);
                 WallDenyStat.JdbcUtils.close(pstmt);
                 WallDenyStat.JdbcUtils.close(conn);
            }

            Thread.sleep(3000);
            System.out.println();
            System.out.println();
        }
    }
}
