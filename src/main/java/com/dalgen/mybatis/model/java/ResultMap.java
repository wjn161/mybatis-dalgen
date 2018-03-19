package com.dalgen.mybatis.model.java;

import com.dalgen.mybatis.model.dbtable.Column;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/13. Desc
 */
public class ResultMap extends DO {
    /**
     * The Name.
     */
    private String       name;
    /**
     * The Type.
     */
    private String       type;
    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();

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
     * Gets column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column) {
        this.columnList.add(column);
    }
}
