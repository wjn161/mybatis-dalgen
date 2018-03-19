package com.dalgen.mybatis.model.config;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class CfResultMap {
    /**
     * The Name.
     */
    private String          name;
    /**
     * The Type. A fully qualified Java class name, or a type alias (see the
     * table above for the list of built-in type aliases).
     */
    private String          type;

    /**
     * The Remark.
     */
    private String remark;
    /**
     * The Columns.
     */
    private List<CfColumn> columns = Lists.newArrayList();

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
     * Gets columns.
     *
     * @return the columns
     */
    public List<CfColumn> getColumns() {
        return columns;
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(CfColumn column) {
        this.columns.add(column);
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
}
