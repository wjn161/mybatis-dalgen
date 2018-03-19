package com.dalgen.mybatis.model.dbtable;

/**
 * Created by bangis.wangdf on 15/12/4. Desc
 */
public class Column implements Comparable {
    /**
     * The Sql type.
     */
    private String sqlType;
    /**
     * The Java type.
     */
    private String javaType;
    /**
     * The Sql name.
     */
    private String sqlName;
    /**
     * The Java name.
     */
    private String javaName;
    /**
     * The Remarks.
     */
    private String remarks;
    /**
     * The Default value.
     */
    private String defaultValue;

    /**
     * Gets remarks.
     *
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks.
     *
     * @param remarks the remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * Gets java type.
     *
     * @return the java type
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Sets java type.
     *
     * @param javaType the java type
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
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
     * Gets default value.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default value.
     *
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Compare to int.
     *
     * @param o the o
     * @return the int
     */
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        }
        String ojavaName = ((Column) o).getJavaName();
        if (this.javaName.length() == ojavaName.length()) {
            return this.javaName.compareTo(((Column) o).getJavaName());
        }
        if (this.javaName.length() > ojavaName.length()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
