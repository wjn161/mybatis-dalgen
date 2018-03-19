package com.dalgen.mybatis.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.dalgen.mybatis.model.Config;
import com.dalgen.mybatis.model.db.DataBase;

/**
 * Created by bangis.wangdf on 15/12/3. Desc
 */
public class ConfigUtil {
    /**
     * The constant config.
     */
    private static Config config    = null;

    /**
     * The constant currentDb.
     */
    private static String currentDb = null;
    /**
     * The constant cmd.
     */
    private static String cmd       = null;

    public static String  dalgenPath;

    /**
     * Read config config.
     *
     * @param configFile the config file
     * @return the config
     * @throws IOException the io exception
     */
    public static Config readConfig(File configFile) throws IOException {
        if (config == null) {
            Document document = Jsoup.parse(configFile, "UTF-8");
            config = new Config();

            //typeMap
            for (Element element : document.getElementsByTag("typemap")) {
                config.addTypeMap(element.attr("from"), element.attr("to"));
            }

            //package
            String defaultPackage = document.getElementsByTag("package").first().val();

            //tablePath
            String tablePath = document.getElementsByTag("tablePath").first().val();

            //database
            for (Element element : document.getElementsByTag("database")) {
                DataBase dataBase = new DataBase();
                dataBase.setName(element.attr("name"));
                dataBase.setDriverClass(element.attr("class"));
                dataBase.setType(element.attr("type"));
                for (Element property : element.getElementsByTag("property")) {
                    dataBase.addProperty(property.attr("name"), property.attr("value"));
                }
                //
                String genPackage = StringUtils.replace(defaultPackage, "${database.name}",
                        dataBase.getName());
                dataBase.setGenPackage(genPackage);
                dataBase.setGenPackagePath(StringUtils.replace(genPackage, ".", "/"));
                String genCommonPackage = StringUtils.replace(defaultPackage, "${database.name}",
                        "common");
                dataBase.setGenDalCommonPackage(genCommonPackage);
                dataBase.setGenDalCommonPackagePath(StringUtils.replace(genCommonPackage, ".", "/"));
                dataBase.setTablePath(StringUtils.replace(tablePath, "${database.name}",
                        dataBase.getName()));
                config.addDataSource(dataBase);
            }
            //tablePrefix
            for (Element element : document.getElementsByTag("tablePrefix")) {
                config.addTablePrefixs(element.val());
            }
            //splitTableSuffix
            for (Element element : document.getElementsByTag("splitTableSuffix")) {
                config.addSplitTableSuffix(element.val());
            }

        }
        return config;
    }

    /**
     * Sets current db.
     *
     * @param currentDb the current db
     */
    public static void setCurrentDb(String currentDb) {
        ConfigUtil.currentDb = currentDb;
    }

    /**
     * Sets cmd.
     *
     * @param cmd the cmd
     */
    public static void setCmd(String cmd) {
        ConfigUtil.cmd = cmd;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public static Config getConfig() {
        return config;
    }

    /**
     * Gets current db.
     *
     * @return the current db
     */
    public static DataBase getCurrentDb() {
        if (config.getDataSourceMap().size() == 1) {
            for (DataBase dataBase : config.getDataSourceMap().values()) {
                return dataBase;
            }
        }
        return config.getDataSourceMap().get(currentDb);
    }

    /**
     * Gets cmd.
     *
     * @return the cmd
     */
    public static String getCmd() {
        return cmd;
    }

}
