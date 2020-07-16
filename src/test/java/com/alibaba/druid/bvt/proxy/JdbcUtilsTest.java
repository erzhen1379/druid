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
package com.alibaba.druid.bvt.proxy;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.druid.wall.WallDenyStat;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.mock.MockConnection;
import com.alibaba.druid.mock.MockResultSet;
import com.alibaba.druid.mock.MockResultSetMetaData;
import com.alibaba.druid.mock.MockStatement;
import com.alibaba.druid.proxy.DruidDriver;
import com.alibaba.druid.stat.JdbcStatManager;
import com.alibaba.druid.util.Utils;

public class JdbcUtilsTest extends TestCase {
    protected void tearDown() throws Exception {
        DruidDriver.getProxyDataSources().clear();
        Assert.assertEquals(0, JdbcStatManager.getInstance().getSqlList().size());
    }
    
    public void test_print() throws Exception {
        final AtomicInteger nextCount = new AtomicInteger(2);

        final MockResultSetMetaData rsMeta = new MockResultSetMetaData() {

            private int[] types = new int[] { Types.DATE, Types.BIT, Types.BOOLEAN, Types.TINYINT,

                                Types.SMALLINT, Types.INTEGER, Types.CLOB, Types.LONGVARCHAR, Types.OTHER,

                                Types.VARCHAR, Types.CHAR, Types.NVARCHAR, Types.NCHAR

                                };

            @Override
            public int getColumnCount() throws SQLException {
                return types.length;
            }

            @Override
            public int getColumnType(int column) throws SQLException {
                return types[column - 1];
            }

            @Override
            public String getColumnName(int column) throws SQLException {
                return "C" + column;
            }
        };

        MockResultSet rs = new MockResultSet(null) {

            @Override
            public boolean next() throws SQLException {
                return nextCount.getAndDecrement() > 0;
            }

            @Override
            public ResultSetMetaData getMetaData() throws SQLException {
                return rsMeta;
            }

            @Override
            public java.sql.Date getDate(int columnIndex) throws SQLException {
                return new java.sql.Date(System.currentTimeMillis());
            }

            @Override
            public boolean wasNull() throws SQLException {
                if (nextCount.get() == 1) {
                    return true;
                }
                return false;
            }

            public Object getObject(int columnIndex) throws SQLException {
                return null;
            }
        };

        WallDenyStat.JdbcUtils.printResultSet(rs);
    }

    public void test_close() throws Exception {
        WallDenyStat.JdbcUtils.close((Connection) null);
        WallDenyStat.JdbcUtils.close((Statement) null);
        WallDenyStat.JdbcUtils.close((ResultSet) null);

        WallDenyStat.JdbcUtils.close(new MockConnection() {

            @Override
            public void close() throws SQLException {
                throw new SQLException();
            }
        });
        WallDenyStat.JdbcUtils.close(new MockStatement(null) {

            @Override
            public void close() throws SQLException {
                throw new SQLException();
            }
        });
        WallDenyStat.JdbcUtils.close(new MockResultSet(null) {

            @Override
            public void close() throws SQLException {
                throw new SQLException();
            }
        });
        WallDenyStat.JdbcUtils.close(new Closeable() {

            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
        WallDenyStat.JdbcUtils.close(new Closeable() {

            @Override
            public void close() throws IOException {
            }
        });
        WallDenyStat.JdbcUtils.close((Closeable) null);

        new WallDenyStat.JdbcUtils();
    }

    public void test_getTypeName() {
        WallDenyStat.JdbcUtils.getTypeName(Types.ARRAY);
        WallDenyStat.JdbcUtils.getTypeName(Types.BIGINT);
        WallDenyStat.JdbcUtils.getTypeName(Types.BINARY);
        WallDenyStat.JdbcUtils.getTypeName(Types.BIT);
        WallDenyStat.JdbcUtils.getTypeName(Types.BLOB);
        WallDenyStat.JdbcUtils.getTypeName(Types.BOOLEAN);
        WallDenyStat.JdbcUtils.getTypeName(Types.CHAR);
        WallDenyStat.JdbcUtils.getTypeName(Types.CLOB);
        WallDenyStat.JdbcUtils.getTypeName(Types.DATALINK);
        WallDenyStat.JdbcUtils.getTypeName(Types.DATE);
        WallDenyStat.JdbcUtils.getTypeName(Types.DECIMAL);
        WallDenyStat.JdbcUtils.getTypeName(Types.DISTINCT);
        WallDenyStat.JdbcUtils.getTypeName(Types.DOUBLE);
        WallDenyStat.JdbcUtils.getTypeName(Types.FLOAT);
        WallDenyStat.JdbcUtils.getTypeName(Types.INTEGER);
        WallDenyStat.JdbcUtils.getTypeName(Types.JAVA_OBJECT);
        WallDenyStat.JdbcUtils.getTypeName(Types.LONGNVARCHAR);
        WallDenyStat.JdbcUtils.getTypeName(Types.LONGVARBINARY);
        WallDenyStat.JdbcUtils.getTypeName(Types.NCHAR);
        WallDenyStat.JdbcUtils.getTypeName(Types.NCLOB);
        WallDenyStat.JdbcUtils.getTypeName(Types.NULL);
        WallDenyStat.JdbcUtils.getTypeName(Types.NUMERIC);
        WallDenyStat.JdbcUtils.getTypeName(Types.NVARCHAR);
        WallDenyStat.JdbcUtils.getTypeName(Types.REAL);
        WallDenyStat.JdbcUtils.getTypeName(Types.REF);
        WallDenyStat.JdbcUtils.getTypeName(Types.ROWID);
        WallDenyStat.JdbcUtils.getTypeName(Types.SMALLINT);
        WallDenyStat.JdbcUtils.getTypeName(Types.SQLXML);
        WallDenyStat.JdbcUtils.getTypeName(Types.STRUCT);
        WallDenyStat.JdbcUtils.getTypeName(Types.TIME);
        WallDenyStat.JdbcUtils.getTypeName(Types.TIMESTAMP);
        WallDenyStat.JdbcUtils.getTypeName(Types.TINYINT);
        WallDenyStat.JdbcUtils.getTypeName(Types.VARBINARY);
        WallDenyStat.JdbcUtils.getTypeName(Types.VARCHAR);
        WallDenyStat.JdbcUtils.getTypeName(Types.OTHER);
    }

    public void test_read() throws Exception {
        {
            Exception error = null;
            try {
                Utils.read(new Reader() {

                    @Override
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        throw new IOException();
                    }

                    @Override
                    public void close() throws IOException {
                        throw new IOException();
                    }

                });
            } catch (RuntimeException ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                Utils.read(new Reader() {

                    @Override
                    public int read(char[] cbuf, int off, int len) throws IOException {
                        throw new IOException();
                    }

                    @Override
                    public void close() throws IOException {
                        throw new IOException();
                    }

                }, 0);
            } catch (RuntimeException ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }

        {
            String text = Utils.read(new Reader() {

                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    return -1;
                }

                @Override
                public void close() throws IOException {
                    throw new IOException();
                }

            }, 1);
            Assert.assertEquals("", text);
        }
        {
            String text = Utils.read(new Reader() {

                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    for (int i = off; i < len; ++i) {
                        cbuf[i] = 'A';
                    }
                    return len;
                }

                @Override
                public void close() throws IOException {
                    throw new IOException();
                }

            }, 2);
            Assert.assertEquals("AA", text);
        }
        {
            Reader reader = new Reader() {

                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    cbuf[off] = 'A';
                    return 1;
                }

                @Override
                public void close() throws IOException {
                    throw new IOException();
                }

            };
            String text = Utils.read(reader, 2);
            Assert.assertEquals("AA", text);
        }
    }

}
