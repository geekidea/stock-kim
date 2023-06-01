/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.framework.util;

import io.geekidea.stock.dto.vo.ColumnAliasName;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-20
 **/
@Slf4j
public class SqlParserUtil {

    private static final String DEFAULT_ALIAS_NAME = "_a";

    /**
     * 清洗SQL
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static String cleanSql(String sql) throws Exception {
        // 排除 -- 开头的注释
        // 排除 # 开头的注释
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        // 删除；
        sql = sql.replace(";", "");
        log.info("清洗前SQL:{}", sql);
        String[] strings = sql.split("\n");
        StringBuffer cleanSql = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            String temp = string.trim();
            if (temp.startsWith("--") || temp.startsWith("#")) {
                continue;
            }
            cleanSql.append(string);
            if (i < strings.length) {
                cleanSql.append("\n");
            }
        }
        log.info("清洗后SQL:{}", cleanSql);
        return cleanSql.toString();
    }

    public static List<ColumnAliasName> getSelectColumnAliasName(String sql) throws Exception {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        CCJSqlParserManager pm = new CCJSqlParserManager();
        Statement statement = pm.parse(new StringReader(sql));
        Select selectStatement = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        List<ColumnAliasName> columnAliasNames = new ArrayList<>();
        int defaultAliasNameIndex = 0;
        for (SelectItem selectItem : selectItems) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            Expression expression = selectExpressionItem.getExpression();
            System.out.println("expression = " + expression);
            Alias alias = selectExpressionItem.getAlias();
            System.out.println("alias = " + alias);
            String aliasName = null;
            if (alias != null) {
                aliasName = alias.getName();
            }

            String columnName = null;
            if (expression instanceof Column) {
                Column column = (Column) expression;
                columnName = column.getColumnName();
            } else if (expression instanceof Function) {
                if (StringUtils.isBlank(aliasName)) {
                    columnName = DEFAULT_ALIAS_NAME + defaultAliasNameIndex;
                    defaultAliasNameIndex++;
                } else {
                    columnName = aliasName;
                }
            }
            ColumnAliasName columnAliasName = new ColumnAliasName();
            columnAliasName.setColumnName(columnName);
            columnAliasName.setAliasName(aliasName);
            columnAliasNames.add(columnAliasName);
        }
        return columnAliasNames;
    }

    public static List<String> getSelectColumns(String sql) throws Exception {
        List<ColumnAliasName> columnAliasNames = getSelectColumnAliasName(sql);
        if (CollectionUtils.isEmpty(columnAliasNames)) {
            return null;
        }
        List<String> columns = new ArrayList<>();
        for (ColumnAliasName columnAliasName : columnAliasNames) {
            String columnName = columnAliasName.getColumnName();
            String aliasName = columnAliasName.getAliasName();
            if (StringUtils.isBlank(aliasName)) {
                columns.add(columnName);
            } else {
                columns.add(aliasName);
            }
        }
        return columns;
    }

}
