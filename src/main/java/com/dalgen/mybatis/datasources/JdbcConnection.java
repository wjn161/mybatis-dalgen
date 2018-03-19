package com.dalgen.mybatis.datasources;

import com.dalgen.mybatis.model.db.DataBase;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.sql.*;

/**
 * Created by bangis.wangdf on 15/12/4. Desc
 */
public class JdbcConnection {

    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * Gets connection.
     *
     * @param dataBase the data base
     * @return the connection
     */
    public static Connection getConnection(DataBase dataBase) {
        try {

            Class.forName(dataBase.getDriverClass());
        } catch (ClassNotFoundException e) {
           LOG.info("驱动加载错误");
        }
        try {

            return DriverManager.getConnection(dataBase.getPropertyMapVal("url"),
                    dataBase.getPropertyMapVal("userid"), dataBase.getPropertyMapVal("password"));
        } catch (SQLException e) {
            LOG.error("获取连接失败", e);
        }
        return null;
    }
}
