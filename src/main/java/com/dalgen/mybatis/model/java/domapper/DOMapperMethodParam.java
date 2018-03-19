package com.dalgen.mybatis.model.java.domapper;

import org.apache.commons.lang.StringUtils;

/**
 * Created by bangis.wangdf on 15/12/13.
 * Desc
 */
public class DOMapperMethodParam {
    /**
     * The Param type.
     */
    private String paramType;
    /**
     * The Param.
     */
    private String param;

    /**
     * Instantiates a new Do mapper method param.
     *
     * @param paramType the param type
     * @param param     the param
     */
    public DOMapperMethodParam( String paramType,String param) {
        this.param = param;
        this.paramType = paramType;
    }

    /**
     * Gets param type.
     *
     * @return the param type
     */
    public String getParamType() {
        return paramType;
    }

    /**
     * Sets param type.
     *
     * @param paramType the param type
     */
    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    /**
     * Gets param.
     *
     * @return the param
     */
    public String getParam() {
        return param;
    }

    /**
     * Sets param.
     *
     * @param param the param
     */
    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        DOMapperMethodParam ob = (DOMapperMethodParam) obj;
        return StringUtils
            .equals(this.getParam() + this.getParamType(), ob.getParam() + ob.getParamType());
    }
}
