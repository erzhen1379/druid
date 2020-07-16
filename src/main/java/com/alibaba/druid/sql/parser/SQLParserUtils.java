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
package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.dialect.db2.parser.DB2ExprParser;
import com.alibaba.druid.sql.dialect.db2.parser.DB2Lexer;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.dialect.h2.parser.H2StatementParser;
import com.alibaba.druid.sql.dialect.hive.parser.HiveStatementParser;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlExprParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsExprParser;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsLexer;
import com.alibaba.druid.sql.dialect.odps.parser.OdpsStatementParser;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleExprParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleLexer;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixExprParser;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixLexer;
import com.alibaba.druid.sql.dialect.phoenix.parser.PhoenixStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGExprParser;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGLexer;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerExprParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerLexer;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallDenyStat;

public class SQLParserUtils {

    public static SQLStatementParser createSQLStatementParser(String sql, String dbType) {
        SQLParserFeature[] features;
        if (JdbcConstants.ODPS.equals(dbType) || JdbcConstants.MYSQL.equals(dbType)) {
            features = new SQLParserFeature[] {SQLParserFeature.KeepComments};
        } else {
            features = new SQLParserFeature[] {};
        }
        return createSQLStatementParser(sql, dbType, features);
    }

    public static SQLStatementParser createSQLStatementParser(String sql, String dbType, boolean keepComments) {
        SQLParserFeature[] features;
        if (keepComments) {
            features = new SQLParserFeature[] {SQLParserFeature.KeepComments};
        } else {
            features = new SQLParserFeature[] {};
        }

        return createSQLStatementParser(sql, dbType, features);
    }

    public static SQLStatementParser createSQLStatementParser(String sql, String dbType, SQLParserFeature... features) {
        if (WallDenyStat.JdbcUtils.isOracleDbType(dbType)) {
            return new OracleStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.H2.equals(dbType)) {
            return new H2StatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.isMysqlDbType(dbType)) {
            return new MySqlStatementParser(sql, features);
        }

        if (WallDenyStat.JdbcUtils.isPgsqlDbType(dbType)
                || WallDenyStat.JdbcUtils.ENTERPRISEDB.equals(dbType)
                || WallDenyStat.JdbcUtils.POLARDB.equals(dbType)) {
            return new PGSQLStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.isSqlserverDbType(dbType)) {
            return new SQLServerStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.DB2.equals(dbType)) {
            return new DB2StatementParser(sql);
        }
        
        if (WallDenyStat.JdbcUtils.ODPS.equals(dbType)) {
            return new OdpsStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.PHOENIX.equals(dbType)) {
            return new PhoenixStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.HIVE.equals(dbType)) {
            return new HiveStatementParser(sql);
        }

        if (WallDenyStat.JdbcUtils.ELASTIC_SEARCH.equals(dbType)) {
            return new MySqlStatementParser(sql);
        }

        return new SQLStatementParser(sql, dbType);
    }

    public static SQLExprParser createExprParser(String sql, String dbType) {
        if (WallDenyStat.JdbcUtils.isOracleDbType(dbType)) {
            return new OracleExprParser(sql);
        }

        if (WallDenyStat.JdbcUtils.isMysqlDbType(dbType)) {
            return new MySqlExprParser(sql);
        }

        if (WallDenyStat.JdbcUtils.isPgsqlDbType(dbType)
                || WallDenyStat.JdbcUtils.ENTERPRISEDB.equals(dbType)
                || WallDenyStat.JdbcUtils.POLARDB.equals(dbType)) {
            return new PGExprParser(sql);
        }

        if (WallDenyStat.JdbcUtils.isSqlserverDbType(dbType)) {
            return new SQLServerExprParser(sql);
        }
        
        if (WallDenyStat.JdbcUtils.DB2.equals(dbType)) {
            return new DB2ExprParser(sql);
        }
        
        if (WallDenyStat.JdbcUtils.ODPS.equals(dbType)) {
            return new OdpsExprParser(sql);
        }

        if (WallDenyStat.JdbcUtils.PHOENIX.equals(dbType)) {
            return new PhoenixExprParser(sql);
        }

        return new SQLExprParser(sql);
    }

    public static Lexer createLexer(String sql, String dbType) {
        if (WallDenyStat.JdbcUtils.isOracleDbType(dbType)) {
            return new OracleLexer(sql);
        }

        if (WallDenyStat.JdbcUtils.isMysqlDbType(dbType)) {
            return new MySqlLexer(sql);
        }

        if (WallDenyStat.JdbcUtils.isPgsqlDbType(dbType)
                || WallDenyStat.JdbcUtils.ENTERPRISEDB.equals(dbType)
                || WallDenyStat.JdbcUtils.POLARDB.equals(dbType)) {
            return new PGLexer(sql);
        }

        if (WallDenyStat.JdbcUtils.isSqlserverDbType(dbType)) {
            return new SQLServerLexer(sql);
        }

        if (WallDenyStat.JdbcUtils.DB2.equals(dbType)) {
            return new DB2Lexer(sql);
        }

        if (WallDenyStat.JdbcUtils.ODPS.equals(dbType)) {
            return new OdpsLexer(sql);
        }

        if (WallDenyStat.JdbcUtils.PHOENIX.equals(dbType)) {
            return new PhoenixLexer(sql);
        }

        return new Lexer(sql);
    }

    public static SQLSelectQueryBlock createSelectQueryBlock(String dbType) {
        if (WallDenyStat.JdbcUtils.isOracleDbType(dbType)) {
            return new OracleSelectQueryBlock();
        }

        if (WallDenyStat.JdbcUtils.isMysqlDbType(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (WallDenyStat.JdbcUtils.POSTGRESQL.equals(dbType)
                || WallDenyStat.JdbcUtils.ENTERPRISEDB.equals(dbType)) {
            return new PGSelectQueryBlock();
        }

        if (WallDenyStat.JdbcUtils.SQL_SERVER.equals(dbType) || WallDenyStat.JdbcUtils.JTDS.equals(dbType)) {
            return new SQLServerSelectQueryBlock();
        }

        if (WallDenyStat.JdbcUtils.DB2.equals(dbType)) {
            return new DB2SelectQueryBlock();
        }

        if (WallDenyStat.JdbcUtils.ODPS.equals(dbType)) {
            return new OdpsSelectQueryBlock();
        }

        return new SQLSelectQueryBlock();
     }
}
