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
package com.github.druid.sql.parser;


import com.alibaba.druid.sql.parser.Lexer;

/**
 * sql解析
 */
public class SQLParser {
    protected final Lexer lexer;
    protected String dbType;

    /**
     * ?
     *
     * @param sql
     * @param dbType
     * @param lexer
     */
    public SQLParser(String sql, String dbType) {
        this(new Lexer(sql, null, dbType), dbType);
        this.lexer.nextToken();
    }

    /**
     * 仅构造
     * @param lexer
     */
    public SQLParser(Lexer lexer) {
        this(lexer,null);
    }



    public SQLParser(String sql) {
        this(sql,null);
    }

    public SQLParser(Lexer lexer, String dbType) {
        this.lexer = lexer;
        this.dbType = dbType;
    }
}
