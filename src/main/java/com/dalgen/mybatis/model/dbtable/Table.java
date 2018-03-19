package com.dalgen.mybatis.model.dbtable;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 表信息 Created by bangis.wangdf on 15/12/4. Desc
 */
public class Table {
    /**
     * The Sql name.
     */
    private String       sqlName;
    /**
     * The Java name.
     */
    private String       javaName;
    /**
     * The Remark.
     */
    private String       remark;
    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();

    /**
     * The Primary keys.
     */
    private PrimaryKeys primaryKeys;

    /**
     * The Physical name.
     */
    private String physicalName;

    /**
     * Gets primary keys.
     *
     * @return the primary keys
     */
    public PrimaryKeys getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * Sets primary keys.
     *
     * @param primaryKeys the primary keys
     */
    public void setPrimaryKeys(PrimaryKeys primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /**
     * Gets sql name.
     *
     * @return the sql name
     */
    public String getSqlName() {
        return sqlName;
    }

    /**
     * Sets sql name.
     *
     * @param sqlName the sql name
     */
    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    /**
     * Gets java name.
     *
     * @return the java name
     */
    public String getJavaName() {
        return javaName;
    }

    /**
     * Sets java name.
     *
     * @param javaName the java name
     */
    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    /**
     * Gets remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        return Lists.sortedCopy(columnList);
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column) {
        this.columnList.add(column);
    }

    /**
     * Sets column list.
     *
     * @param columnList the column list
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * Gets physical name.
     *
     * @return the physical name
     */
    public String getPhysicalName() {
        return physicalName;
    }

    /**
     * Sets physical name.
     *
     * @param physicalName the physical name
     */
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }
}
