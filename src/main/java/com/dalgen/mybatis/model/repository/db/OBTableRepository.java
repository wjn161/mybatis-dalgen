package com.dalgen.mybatis.model.repository.db;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.type.JdbcType;

import com.dalgen.mybatis.enums.TypeMapEnum;
import com.dalgen.mybatis.model.config.CfColumn;
import com.dalgen.mybatis.model.config.CfTable;
import com.dalgen.mybatis.model.dbtable.Column;
import com.dalgen.mybatis.model.dbtable.PrimaryKeys;
import com.dalgen.mybatis.model.dbtable.Table;
import com.dalgen.mybatis.utils.CamelCaseUtils;
import com.dalgen.mybatis.utils.ConfigUtil;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class OBTableRepository {

    /**
     * The constant PRIMARY_KEY_PATTERN.
     */
//primaryKey
    private static final Pattern PRIMARY_KEY_PATTERN = Pattern.compile("\\((.*)\\)");

    /**
     * Gain table table.
     *
     * @param connection the connection
     * @param tableName  the table name
     * @param cfTable    the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    public Table gainTable(Connection connection, String tableName, CfTable cfTable)
            throws SQLException {
        //支持分表,逻辑表
        String physicalName = cfTable == null ? tableName : cfTable.getPhysicalName();
        //物理表
        String logicName = tableName;
        for (String splitTableSuffix : ConfigUtil.getConfig().getSplitTableSuffixs()) {
            if (StringUtils.endsWithIgnoreCase(tableName, splitTableSuffix)) {
                logicName = StringUtils.replace(logicName, splitTableSuffix, "");
                break;
            }
        }
        //自定义字段类型
        List<CfColumn> cfColumns = cfTable == null ? null : cfTable.getColumns();
        //生成table
        Table table = new Table();
        table.setSqlName(logicName);
        for (String pre : ConfigUtil.getConfig().getTablePrefixs()) {
            if (!StringUtils.endsWith(pre, "_")) {
                pre = pre + "_";
            }

            if (StringUtils.startsWith(logicName, StringUtils.upperCase(pre))) {
                table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(StringUtils.substring(
                        logicName, pre.length())));
                break;/* 取第一个匹配的 */
            }
        }
        if (StringUtils.isBlank(table.getJavaName())) {
            table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(logicName));
        }
        table.setPhysicalName(physicalName);
        table.setRemark(logicName);
        //填充字段
        fillColumns(connection, physicalName, table, cfColumns);
        return table;
    }

    /**
     * Fill columns.
     *
     * @param connection the connection
     * @param tableName  the table name
     * @param table      the table
     * @param cfColumns  the cf columns
     * @throws SQLException the sql exception
     */
    private void fillColumns(Connection connection, String tableName, Table table,
                             List<CfColumn> cfColumns) throws SQLException {
        //指定表字段
        ResultSet resultSet = connection.createStatement().executeQuery(
                "SHOW CREATE TABLE " + tableName);
        //组装字段
        while (resultSet.next()) {
            //得到建表语句
            String createTableSql = getCreateTableSql(resultSet);

            //主键行
            String primaryKeyLine = null;

            //解析建表语句
            String[] createSqlLines = StringUtils.split(createTableSql, "\n");

            //准备字段
            Map<String, Column> columnMap = Maps.newHashMap();
            primaryKeyLine = preColumns(table, columnMap, cfColumns, primaryKeyLine, createSqlLines);

            //最后一行解析表注释
            String lastLine = createSqlLines[createSqlLines.length - 1];
            for (String comments : StringUtils.split(lastLine)) {
                if (StringUtils.startsWith(comments, "COMMENT=")) {
                    table.setRemark(comments.split("=", 2)[1]);
                }
            }

            //设置主键
            if (primaryKeyLine != null) {
                Matcher m = PRIMARY_KEY_PATTERN.matcher(primaryKeyLine);
                while (m.find()) {
                    PrimaryKeys primaryKeys = new PrimaryKeys();
                    primaryKeys.setPkName("PrimaryKey");
                    String[] pks = StringUtils.split(m.group(1));
                    for (String pk : pks) {
                        pk = StringUtils.trim(pk);
                        if (pks.length == 1) {
                            primaryKeys.setPkName(CamelCaseUtils.toCapitalizeCamelCase(pk));
                            primaryKeys.addColumn(columnMap.get(pk));
                        } else {
                            primaryKeys.addColumn(columnMap.get(pk));
                        }
                    }
                    table.setPrimaryKeys(primaryKeys);
                }
            }
        }
    }

    /**
     * Gets create table sql.
     *
     * @param resultSet the result set
     * @return the create table sql
     * @throws SQLException the sql exception
     */
    private String getCreateTableSql(ResultSet resultSet) throws SQLException {
        String createTableSql = StringUtils.upperCase(resultSet.getString(2));
        createTableSql = StringUtils.replace(createTableSql, "`", "");
        createTableSql = createTableSql.replaceAll("\\s{1,}=\\s{1,}", "=");
        createTableSql = createTableSql.replaceAll("\\(\\d*\\)", "");
        createTableSql = createTableSql.replaceAll("COMMENT\\s{1,}'", "COMMON='");
        createTableSql = createTableSql.replaceAll(", ", " ");
        createTableSql = createTableSql.replaceAll(",", "");
        createTableSql = createTableSql.replaceAll("'", "");
        return createTableSql;
    }

    /**
     * Pre columns string.
     *
     * @param table          the table
     * @param columnMap      the column map
     * @param cfColumns      the cf columns
     * @param primaryKeyLine the primary key line
     * @param createSqlLines the create sql lines
     * @return the string
     */
    private String preColumns(Table table, Map<String, Column> columnMap, List<CfColumn> cfColumns, String primaryKeyLine, String[] createSqlLines) {
        for (int i = 1; i < createSqlLines.length - 1; i++) {
            String createSqlLine = StringUtils.trim(createSqlLines[i]);
            if (StringUtils.startsWith(createSqlLine, "PRIMARY KEY")) {//主键
                primaryKeyLine = createSqlLine;
                continue;
            }
            if (StringUtils.startsWith(createSqlLine, "KEY ") || StringUtils.isBlank(createSqlLine)) {//索引,外键啥的,不处理
                continue;
            }
            Column column = new Column();
            String[] columnArray = StringUtils.split(createSqlLine);
            column.setSqlName(columnArray[0]);
            column.setSqlType(TypeMapEnum.getByJdbcType(columnArray[1]).getJdbcType());
            column.setJavaName(CamelCaseUtils.toCamelCase(column.getSqlName()));
            column.setJavaType(getJavaType(column, cfColumns));
            if (StringUtils.startsWith(columnArray[columnArray.length - 1], "COMMENT=")) {
                column.setRemarks(columnArray[columnArray.length - 1].split("=", 2)[1]);
            }
            if (StringUtils.isBlank(column.getRemarks())) {
                column.setRemarks(column.getSqlName());
            }
            table.addColumn(column);
            columnMap.put(column.getSqlName(), column);
        }
        return primaryKeyLine;
    }

    /**
     * Gets java type.
     *
     * @param column    the column
     * @param cfColumns the cf columns
     * @return the java type
     */
    private String getJavaType(Column column, List<CfColumn> cfColumns) {
        if (cfColumns != null && cfColumns.size() > 0) {
            for (CfColumn cfColumn : cfColumns) {
                if (StringUtils.endsWithIgnoreCase(column.getSqlName(), cfColumn.getName())) {
                    return cfColumn.getJavatype();
                }
            }
        }
        String javaType = TypeMapEnum.getByJdbcType(column.getSqlType()).getJavaType();
        String custJavaType = ConfigUtil.getConfig().getTypeMap().get(javaType);
        return StringUtils.isBlank(custJavaType) ? javaType : custJavaType;
    }
}
