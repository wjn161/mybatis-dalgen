package com.dalgen.mybatis.dataloaders;

import com.dalgen.mybatis.datasources.DBConnectionFactory;
import com.dalgen.mybatis.model.Gen;
import com.dalgen.mybatis.model.config.CfTable;
import com.dalgen.mybatis.model.dbtable.Table;
import com.dalgen.mybatis.utils.ConfigUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fmpp.Engine;
import fmpp.tdd.DataLoader;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/12. Desc
 */
public abstract class AbstractDalgenLoader implements DataLoader {

    /**
     * Load object.
     *
     * @param e the e
     * @param args Arguments that the caller specifies for this directive call.
     *            Not null. The implementation should check if it understands
     *            all arguments, and it should throw
     *            <code>java.lang.IllegalArgumentException</code> if it doesn't.
     * @return The object that will be accessed in FreeMarker templates. The
     *         object can be of any type. FreeMarker will wrap the object so
     *         that it is visible as an FTL variable. However, if the object
     *         implements <code>freemarker.template.TemplateModel</code>, then
     *         it will not be wrapped, as it is already an FTL variable.
     * @throws Exception the exception
     */
    public Object load(Engine e, List args) throws Exception {
        Gen gen = new Gen();

        gen.setOutRoot(e.getOutputRoot().getAbsolutePath());
        gen.setDalgenRoot(ConfigUtil.dalgenPath);
        gen.setDataBaseName(ConfigUtil.getCurrentDb().getName());
        gen.setTablesPath(ConfigUtil.getCurrentDb().getName() + "Tables");

        File tablesFile = new File(
                (gen.getDalgenRoot() + File.separator + gen.getTablesPath() + File.separator));
        if (!tablesFile.exists()) {
            tablesFile.mkdir();
        }

        Connection connection = null;
        try {
            connection = DBConnectionFactory.getConnection();
            gen.setDbType(connection.getMetaData().getDatabaseProductName());
            load(gen, connection, tablesFile);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return gen;
    }

    /**
     * Load.
     *
     * @param gen the gen
     * @param connection the connection
     * @throws SQLException the sql exception
     */
    public abstract void load(Gen gen, Connection connection, File tablesFile) throws Exception;

    /**
     * File 2 db name string.
     *
     * @param tableFile the table file
     * @return the string
     */
    protected String file2DbName(File tableFile) {
        return StringUtils.upperCase(StringUtils.substring(tableFile.getName(), 0, tableFile
                .getName().indexOf(".")));
    }
}
