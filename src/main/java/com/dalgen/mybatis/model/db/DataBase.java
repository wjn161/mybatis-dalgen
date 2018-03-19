package com.dalgen.mybatis.model.db;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/3. Desc
 */
public class DataBase {
    /**
     * The Name.
     */
    private String              name;
    /**
     * The Driver class.
     */
    private String              driverClass;
    /**
     * The Type.
     */
    private String              type;
    /**
     * The Gen package.
     */
    private String              genPackage;

    /**
     * The Gen package path.
     */
    private String              genPackagePath;

    /**
     * The Gen dal common package.
     */
    private String              genDalCommonPackage;

    /**
     * The Gen dal common package path.
     */
    private String              genDalCommonPackagePath;
    /**
     * The Table path.
     */
    private String              tablePath;
    /**
     * The Property map.
     */
    private Map<String, String> propertyMap = Maps.newHashMap();

    /**
     * Gets gen package.
     *
     * @return the gen package
     */
    public String getGenPackage() {
        return genPackage;
    }

    /**
     * Sets gen package.
     *
     * @param genPackage the gen package
     */
    public void setGenPackage(String genPackage) {
        this.genPackage = genPackage;
    }

    /**
     * Gets gen package path.
     *
     * @return the gen package path
     */
    public String getGenPackagePath() {
        return genPackagePath;
    }

    /**
     * Sets gen package path.
     *
     * @param genPackagePath the gen package path
     */
    public void setGenPackagePath(String genPackagePath) {
        this.genPackagePath = genPackagePath;
    }

    /**
     * Gets table path.
     *
     * @return the table path
     */
    public String getTablePath() {
        return tablePath;
    }

    /**
     * Sets table path.
     *
     * @param tablePath the table path
     */
    public void setTablePath(String tablePath) {
        this.tablePath = tablePath;
    }

    /**
     * Add property.
     *
     * @param key   the key
     * @param value the value
     */
    public void addProperty(String key, String value) {
        this.propertyMap.put(key, value);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets driver class.
     *
     * @return the driver class
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Sets driver class.
     *
     * @param driverClass the driver class
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets property map.
     *
     * @return the property map
     */
    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Gets property map val.
     *
     * @param key the key
     * @return the property map val
     */
    public String getPropertyMapVal(String key) {
        return this.propertyMap.get(key);
    }

    /**
     * Sets property map.
     *
     * @param propertyMap the property map
     */
    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    /**
     * Gets gen dal common package.
     *
     * @return the gen dal common package
     */
    public String getGenDalCommonPackage() {
        return genDalCommonPackage;
    }

    /**
     * Sets gen dal common package.
     *
     * @param genDalCommonPackage the gen dal common package
     */
    public void setGenDalCommonPackage(String genDalCommonPackage) {
        this.genDalCommonPackage = genDalCommonPackage;
    }

    /**
     * Gets gen dal common package path.
     *
     * @return the gen dal common package path
     */
    public String getGenDalCommonPackagePath() {
        return genDalCommonPackagePath;
    }

    /**
     * Sets gen dal common package path.
     *
     * @param genDalCommonPackagePath the gen dal common package path
     */
    public void setGenDalCommonPackagePath(String genDalCommonPackagePath) {
        this.genDalCommonPackagePath = genDalCommonPackagePath;
    }
}
