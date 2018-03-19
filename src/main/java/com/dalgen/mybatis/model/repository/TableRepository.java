package com.dalgen.mybatis.model.repository;

import com.dalgen.mybatis.enums.TypeMapEnum;
import com.dalgen.mybatis.model.config.CfColumn;
import com.dalgen.mybatis.model.config.CfTable;
import com.dalgen.mybatis.model.dbtable.Column;
import com.dalgen.mybatis.model.dbtable.PrimaryKeys;
import com.dalgen.mybatis.model.dbtable.Table;
import com.dalgen.mybatis.model.repository.db.MySQLTableRepository;
import com.dalgen.mybatis.model.repository.db.OBTableRepository;
import com.dalgen.mybatis.utils.CamelCaseUtils;
import com.dalgen.mybatis.utils.ConfigUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.type.JdbcType;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class TableRepository {
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
        if (StringUtils.equalsIgnoreCase(ConfigUtil.getCurrentDb().getType(), "mysql")) {
            MySQLTableRepository tableRepository = new MySQLTableRepository();
            return tableRepository.gainTable(connection,tableName,cfTable);
        }
        if (StringUtils.equalsIgnoreCase(ConfigUtil.getCurrentDb().getType(), "ob")) {
            OBTableRepository tableRepository = new OBTableRepository();
            return tableRepository.gainTable(connection,tableName,cfTable);
        }

        Validate.notNull(null,"===== config.xml 目前仅支持 mysql ob 请正确选择 =====");
        return null;
    }
}
