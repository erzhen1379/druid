package com.github.druid.sql.dialect.mysql;

import com.alibaba.druid.sql.parser.Lexer;
import com.github.druid.sql.parser.SQLParser;

public class MySqlLexer extends SQLParser {
    public MySqlLexer(String sql, String dbType) {
        super(sql, dbType);
    }

    public MySqlLexer(Lexer lexer) {
        super(lexer);
    }

    public MySqlLexer(String sql) {
        super(sql);
    }

    public MySqlLexer(Lexer lexer, String dbType) {
        super(lexer, dbType);
    }
}
