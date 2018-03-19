package com.dalgen.mybatis.model.dbtable;

import com.dalgen.mybatis.model.dbtable.Column;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/5.
 * Desc
 */
public class PrimaryKeys {
    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();
    /**
     * The Pk name.
     */
    private String pkName;

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column){
        this.columnList.add(column);
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
     * Sets column list.
     *
     * @param columnList the column list
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * Gets pk name.
     *
     * @return the pk name
     */
    public String getPkName() {
        return pkName;
    }

    /**
     * Sets pk name.
     *
     * @param pkName the pk name
     */
    public void setPkName(String pkName) {
        this.pkName = pkName;
    }
}
