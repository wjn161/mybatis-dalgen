package com.dalgen.mybatis.model.repository.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
public class MySQLTableRepository {
    /**
     * The constant .
     */
    private static final String[] TABLES_TYPE = { "TABLE" };

    /**
     * Gain table table.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param cfTable the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    public Table gainTable(Connection connection, String tableName, CfTable cfTable)
            throws SQLException {
        String physicalName = cfTable == null ? tableName : cfTable.getPhysicalName();
        String logicName = tableName;
        for (String splitTableSuffix : ConfigUtil.getConfig().getSplitTableSuffixs()) {
            if (StringUtils.endsWithIgnoreCase(tableName, splitTableSuffix)) {
                logicName = StringUtils.replace(logicName, splitTableSuffix, "");
                break;
            }
        }

        List<CfColumn> cfColumns = cfTable == null ? null : cfTable.getColumns();
        DatabaseMetaData databaseMetaData = connection.getMetaData();


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
        fillColumns(connection, physicalName, databaseMetaData, table, cfColumns);

        //主键
        fillPrimaryKeys(connection, physicalName, databaseMetaData, table);
        return table;
    }

    /**
     * Fill primary keys.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param databaseMetaData the database meta data
     * @param table the table
     * @throws SQLException the sql exception
     */
    private void fillPrimaryKeys(Connection connection, String tableName,
                                 DatabaseMetaData databaseMetaData, Table table)
            throws SQLException {
        PrimaryKeys primaryKeys = null;

        ResultSet resultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(),
                connection.getSchema(), tableName);

        while (resultSet.next()) {
            for (Column column : table.getColumnList()) {
                if (StringUtils.equals(column.getSqlName(), Str(resultSet, "COLUMN_NAME"))) {
                    primaryKeys = primaryKeys == null ? new PrimaryKeys() : primaryKeys;
                    primaryKeys.addColumn(column);
                    String pkName = resultSet.getString("PK_NAME");
                    pkName = StringUtils.isBlank(pkName) ? column.getSqlName() : pkName;
                    primaryKeys.setPkName(CamelCaseUtils.toCapitalizeCamelCase(pkName));
                }
            }
        }
        table.setPrimaryKeys(primaryKeys);
    }

    /**
     * Fill columns.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param databaseMetaData the database meta data
     * @param table the table
     * @param cfColumns the cf columns
     * @throws SQLException the sql exception
     */
    private void fillColumns(Connection connection, String tableName,
                             DatabaseMetaData databaseMetaData, Table table,
                             List<CfColumn> cfColumns) throws SQLException {
        //指定表字段
        ResultSet resultSet = databaseMetaData.getColumns(connection.getCatalog(), null, tableName,
                null);

        //组装字段
        while (resultSet.next()) {
            Column column = new Column();
            column.setSqlName(Str(resultSet, "COLUMN_NAME"));
            column.setSqlType(JdbcType.forCode(resultSet.getInt("DATA_TYPE")).name());
            column.setDefaultValue(Str(resultSet, "COLUMN_DEF"));
            column.setJavaName(CamelCaseUtils.toCamelCase(column.getSqlName()));
            column.setJavaType(getJavaType(column, cfColumns));
            column.setRemarks(Str(resultSet, "REMARKS", column.getSqlName()));
            table.addColumn(column);
        }
    }

    /**
     * Gets java type.
     *
     * @param column the column
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

    /**
     * Str string.
     *
     * @param resultSet the result set
     * @param column the column def
     * @return the string
     * @throws SQLException the sql exception
     */
    private String Str(ResultSet resultSet, String column) throws SQLException {
        return StringUtils.upperCase(resultSet.getString(column));
    }

    /**
     * Str string.
     *
     * @param resultSet the result set
     * @param column the column
     * @param defaultVal the default val
     * @return the string
     * @throws SQLException the sql exception
     */
    private String Str(ResultSet resultSet, String column, String defaultVal) throws SQLException {
        String val = Str(resultSet, column);
        return StringUtils.isBlank(val) ? defaultVal : val;
    }
}
