/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.sql;

import com.alibaba.druid.wall.WallDenyStat;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.sql.SQLUtils;

public class DistinctTest extends TestCase {

    private String sql = "select count(distinct *) from t";

    public void test_mysql() throws Exception {
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.MYSQL));
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.OCEANBASE));
    }

    public void test_oracle() throws Exception {
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.ORACLE));
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.OCEANBASE_ORACLE));
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.ALI_ORACLE));
    }

    public void test_oracle_unique() throws Exception {
        Assert.assertEquals("SELECT count(UNIQUE *)\nFROM t",
                            SQLUtils.format("select count(unique *) from t", WallDenyStat.JdbcUtils.ORACLE));
        Assert.assertEquals("SELECT count(UNIQUE *)\nFROM t",
                SQLUtils.format("select count(unique *) from t", WallDenyStat.JdbcUtils.OCEANBASE_ORACLE));
        Assert.assertEquals("SELECT count(UNIQUE *)\nFROM t",
                            SQLUtils.format("select count(unique *) from t", WallDenyStat.JdbcUtils.ALI_ORACLE));
    }

    public void test_postgres() throws Exception {
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, WallDenyStat.JdbcUtils.POSTGRESQL));
    }

    public void test_sql92() throws Exception {
        Assert.assertEquals("SELECT count(DISTINCT *)\nFROM t", SQLUtils.format(sql, null));
    }
}
