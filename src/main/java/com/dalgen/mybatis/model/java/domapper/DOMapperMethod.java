package com.dalgen.mybatis.model.java.domapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/13. Desc
 */
public class DOMapperMethod implements Cloneable {
    /**
     * The Return class.
     */
    private String                    returnClass;
    /**
     * The Name.
     */
    private String                    name;

    /**
     * The Paging name.
     */
    private String                    pagingName;

    /**
     * The Desc.
     */
    private String                    desc;
    /**
     * The Sql.
     */
    private String                    sql;

    /**
     * The Is paging.
     */
    private String                    pagingFlag = "false";

    /**
     * The Params.
     */
    private List<DOMapperMethodParam> params     = Lists.newArrayList();

    /**
     * Gets return class.
     *
     * @return the return class
     */
    public String getReturnClass() {
        return returnClass;
    }

    /**
     * Sets return class.
     *
     * @param returnClass the return class
     */
    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
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
     * Gets params.
     *
     * @return the params
     */
    public List<DOMapperMethodParam> getParams() {
        //对params进行排序
        Ordering<DOMapperMethodParam> byLengthOrdering = new Ordering<DOMapperMethodParam>() {
            private static final long serialVersionUID = 2293951554121638998L;

            public int compare(DOMapperMethodParam left, DOMapperMethodParam right) {
                int cr = compare(left.getParamType(), right.getParamType());
                return cr == 0 ? compare(left.getParam(), right.getParam()) : cr;
            }

            private int compare(String left, String right) {
                int cr = Ints.compare(left.length(), right.length());
                return cr == 0 ? left.compareTo(right) : cr;
            }

        };
        return byLengthOrdering.sortedCopy(params);
    }

    /**
     * Add param.
     *
     * @param param the param
     */
    public void addParam(DOMapperMethodParam param) {
        if(!this.params.contains(param)) {
            this.params.add(param);
        }
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(List<DOMapperMethodParam> params) {
        this.params = params;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets sql.
     *
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * Sets sql.
     *
     * @param sql the sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Gets paging flag.
     *
     * @return the paging flag
     */
    public String getPagingFlag() {
        return pagingFlag;
    }

    /**
     * Sets paging flag.
     *
     * @param pagingFlag the paging flag
     */
    public void setPagingFlag(String pagingFlag) {
        this.pagingFlag = pagingFlag;
    }

    /**
     * Gets paging name.
     *
     * @return the paging name
     */
    public String getPagingName() {
        return pagingName;
    }

    /**
     * Sets paging name.
     *
     * @param pagingName the paging name
     */
    public void setPagingName(String pagingName) {
        this.pagingName = pagingName;
    }
}
