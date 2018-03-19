package com.dalgen.mybatis.model;

import com.dalgen.mybatis.model.db.DataBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/3. 数据源相关配置
 */
public class Config {
    /**
     * The Data source map.
     */
    private Map<String, DataBase> dataSourceMap     = Maps.newHashMap();
    /**
     * The Type map.
     */
    private Map<String, String>   typeMap           = Maps.newHashMap();
    /**
     * The Table prefixs.
     */
    private List<String>          tablePrefixs      = Lists.newArrayList();

    /**
     * The Split table suffixs.
     */
    private List<String>          splitTableSuffixs = Lists.newArrayList();

    /**
     * Add data source.
     *
     * @param dataBase the data base
     */
    public void addDataSource(DataBase dataBase) {
        this.dataSourceMap.put(dataBase.getName(), dataBase);
    }

    /**
     * Gets data source map.
     *
     * @return the data source map
     */
    public Map<String, DataBase> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * Gets type map.
     *
     * @return the type map
     */
    public Map<String, String> getTypeMap() {
        return typeMap;
    }

    /**
     * Add type map.
     *
     * @param type the type
     * @param to the to
     */
    public void addTypeMap(String type, String to) {
        this.typeMap.put(type, to);
    }

    /**
     * Gets table prefixs.
     *
     * @return the table prefixs
     */
    public List<String> getTablePrefixs() {
        return tablePrefixs;
    }

    /**
     * Add table prefixs.
     *
     * @param tablePrefix the table prefix
     */
    public void addTablePrefixs(String tablePrefix) {
        this.tablePrefixs.add(tablePrefix);
    }

    /**
     * Gets split table suffixs.
     *
     * @return the split table suffixs
     */
    public List<String> getSplitTableSuffixs() {
        return splitTableSuffixs;
    }

    /**
     * Add split table suffix.
     *
     * @param splitTableSuffix the split table suffix
     */
    public void addSplitTableSuffix(String splitTableSuffix) {
        this.splitTableSuffixs.add(splitTableSuffix);
    }
}
