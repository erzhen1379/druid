package com.github.test.wisezone.utils;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**
 * 配置mysql连接数据源
 */
public class DataSourceUtil {
    //单例模式
    private static DataSourceUtil instance = new DataSourceUtil();

    private DataSourceUtil() {
    }

    public static DataSourceUtil getInstance() {
        return instance;
    }

    //和当前线程相关，通过Threadlocal保存当前线程的连接对象
    private ThreadLocal<Connection> t1 = new ThreadLocal<Connection>();

    //log4j日志
    private static Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);
    private static DruidDataSource dataSource = null;

    static {
        Properties properties = new Properties();
        InputStream in = DataSourceUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        System.out.println(in);
        try {
            properties.load(in);
            System.out.println("properties->" + properties);
            //创建DataSourceSource实例
            /**
             * 此处为什么不用DruidSource？
             */
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);

            logger.info("初始化数据源成功");
            logger.error("初始化数据源成功");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("jdbc.properties加载失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立连接
     *
     * @return
     */
    public Connection getConnection() {
        /*
         * 每次获取连接时，先从ThreadLocal尝试取一个
         * */
        Connection connection = this.t1.get();
        //如果第一次获取连接，connection必定为null
        if (connection == null) {
            try {
                //通过数据源，从连接池中获取一个连接
                System.out.println("dataSource->" + dataSource);
                Connection newConnection = dataSource.getConnection();

                System.out.println("newConnection->" + newConnection);
                this.t1.set(newConnection);
                return newConnection;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        //直接return从ThreadLocal中获取的Connection
        return connection;
    }


    /**
     * 通用的增、删、改的方法
     *
     * @param sql
     * @param params 可变长度的参数列表，当成一个数组用
     * @return
     */
    public int executeUpdate(String sql, Object... params) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            //为sql语句的参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            int row = pstmt.executeUpdate();
            logger.info("执行sql语句：" + sql);
            logger.info("返回受影响的行数：" + row);
            return row;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally { //不管程序是否出现异常都会执行的代码块
            closePstmt(pstmt);
            closeConnection(conn);
            logger.info("关闭连接，释放资源");
        }
        return -1;
    }

    /**
     * 通用的查询，sql中查询的列必须和javaBean中的属性名一致
     *
     * @param tClass 查询的javaBean,必须有无参构造方法
     * @param sql    查询的sql语句
     * @param params sql语句的参数
     * @param <T>    泛型
     * @return 集合
     */
    public <T> List<T> queryList(Class<T> tClass, String sql, Object... params) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<T>();
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                T t = tClass.newInstance();
                //为对象t的属性赋值
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();//获取列的个数
                for (int i = 0; i < columnCount; i++) {
                    String c_name = metaData.getColumnLabel(i + 1); //获取列名
                    BeanUtils.setProperty(t, c_name, rs.getObject(c_name));
                }

                //将对象t插入到集合中
                list.add(t);
                logger.info("组建对象：" + t);
            }
            logger.info("返回集合长度为：" + list.size());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            closeRs(rs);
            closePstmt(pstmt);
            closeConnection(conn);
        }
        return null;
    }

    /**
     * 查询并返回一条数据
     *
     * @param tClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> T queryOne(Class<T> tClass, String sql, Object... params) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            T t = tClass.newInstance();

            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                for (int j = 0; j < metaData.getColumnCount(); j++) {
                    String name = metaData.getColumnLabel(j + 1); //列名
                    Object value = rs.getObject(name); //根据列名取值
                    BeanUtils.setProperty(t, name, value);
                }
            }

            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            closeRs(rs);
            closePstmt(pstmt);
            closeConnection(conn);
        }
        return null;
    }

    /**
     * 这种查询适合用于多表连查
     *
     * @param sql
     * @param params
     * @return
     */
    public List<Map> queryMap(String sql, Object... params) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map> list = new ArrayList<Map>();
        try {
            pstmt = conn.prepareStatement(sql);
            if (params != null)
                for (int i = 0; i < params.length; i++)
                    pstmt.setObject(i + 1, params[i]);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                Map map = new HashMap();//通过map存一行数据
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String key = metaData.getColumnLabel(i + 1);
                    Object value = rs.getObject(key);
                    map.put(key, value);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeRs(rs);
            closePstmt(pstmt);
            closeConnection(conn);
        }
        return list;
    }

    /**
     * 关闭处理sql语句的对象
     *
     * @param pstmt
     */
    public void closePstmt(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据集
     *
     * @param rs
     */
    public void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接，将Connection返还到连接池
     *
     * @param connection
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                this.t1.remove();//从当前线程中移除connection
                connection.close();//将connection对象返还到连接池中
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
