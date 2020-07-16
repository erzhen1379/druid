package com.alibaba.druid.bvt.proxy;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl;
import junit.framework.TestCase;

import java.lang.reflect.Field;

/**
 * Created by wenshao on 12/07/2017.
 */
public class InsertValues_2 extends TestCase {
    public void test_insert_values() throws Exception {
        String sql = "insert into t (f0, f1, f2, f3, f4) values (\"????????????????????????\", \"????????????????????????\", \"????????????????????????\", \"????????????????????????\", \"????????????????????????\")";


        PreparedStatementProxyImpl proxy = new PreparedStatementProxyImpl(null, null, sql, 0);

        Field field = PreparedStatementProxyImpl.class.getDeclaredField("parameters");
        field.setAccessible(true);
        JdbcParameter[] params = (JdbcParameter[]) field.get(proxy);
        assertEquals(0, params.length);
    }
}
