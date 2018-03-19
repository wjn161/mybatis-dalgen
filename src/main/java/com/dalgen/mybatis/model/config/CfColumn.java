package com.dalgen.mybatis.model.config;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class CfColumn {
    /**
     * The Name.
     */
    private String name;
    /**
     * The Javatype.
     */
    private String javatype;

    /**
     * The Sql type.
     */
    private String sqlType;

    /**
     * The Remark.
     */
    private String remark;
    /**
     * The Related column.
     */
    private String relatedColumn;

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
     * Gets javatype.
     *
     * @return the javatype
     */
    public String getJavatype() {
        return javatype;
    }

    /**
     * Sets javatype.
     *
     * @param javatype the javatype
     */
    public void setJavatype(String javatype) {
        this.javatype = javatype;
    }

    /**
     * Gets related column.
     *
     * @return the related column
     */
    public String getRelatedColumn() {
        return relatedColumn;
    }

    /**
     * Sets related column.
     *
     * @param relatedColumn the related column
     */
    public void setRelatedColumn(String relatedColumn) {
        this.relatedColumn = relatedColumn;
    }

    /**
     * Gets sql type.
     *
     * @return the sql type
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * Sets sql type.
     *
     * @param sqlType the sql type
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
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
