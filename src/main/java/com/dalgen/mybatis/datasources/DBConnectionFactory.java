package com.dalgen.mybatis.datasources;

import java.sql.Connection;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.dalgen.mybatis.utils.ConfigUtil;
/**
 * Created by bangis.wangdf on 15/12/4. Desc
 */
public class DBConnectionFactory {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            LOG.info("==== init connection");
            if (connection == null) {
                connection = JdbcConnection.getConnection(ConfigUtil.getCurrentDb());
                Validate.notNull(connection, "====connection error mysql");

            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return connection;

    }

}
