package com.github.test.wisezone.sqlParse;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

/**
 * 解析sql模块
 */
public class ParserMain {
    public static void main(String[] args) {
        System.out.println("执行解析模块");
        String sql = "select * from examstudent order by flow_id";
        //创建sql解析模块
        MySqlStatementParser mySqlStatementParser = new MySqlStatementParser(sql);
        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement stat = mySqlStatementParser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stat.accept(visitor);
        System.out.println(visitor.getColumns());
        System.out.println(visitor.getOrderByColumns());

    }
}
