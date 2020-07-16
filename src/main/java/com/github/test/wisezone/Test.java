package com.github.test.wisezone;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class Test {

    public static void main(String[] args) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("D:\\javaproject\\druid-master\\src\\main\\resources\\log4j.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获得连接:
            conn = dataSource.getConnection();
            // 编写SQL：
            String sql = "select * from examstudent";
            pstmt = conn.prepareStatement(sql);
            // 执行sql:
            rs = pstmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt("flow_id"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
