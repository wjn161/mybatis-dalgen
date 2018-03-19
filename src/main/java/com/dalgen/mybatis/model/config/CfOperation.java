package com.dalgen.mybatis.model.config;

import com.dalgen.mybatis.enums.MultiplicityEnum;
import com.dalgen.mybatis.enums.ParamTypeEnum;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class CfOperation {

    /**
     * The Name.
     */
    private String              name;
    /**
     * Paging param name
     */
    private String              paging;
    /**
     * The Param type.
     */
    private ParamTypeEnum       paramType;
    /**
     * The Multiplicity.
     */
    private MultiplicityEnum    multiplicity;
    /**
     * The Remark.
     */
    private String              remark;
    /**
     * The Resulttype.
     */
    private String              resulttype;
    /**
     * The Resultmap.
     */
    private String              resultmap;
    /**
     * The Timeout.
     */
    private Long                timeout;

    /**
     * The Cdata.
     */
    private String              cdata;

    /**
     * The Cdata page count.
     */
    private String              cdataPageCount;

    /**
     * The Sql desc.
     */
    private String              sqlDesc;

    /**
     * The Primitive params.
     */
    private Map<String, String> primitiveParams        = Maps.newHashMap();

    /**
     * The Primitive foreach params.
     */
    private Map<String, String> primitiveForeachParams = Maps.newHashMap();

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
     * Gets paging.
     *
     * @return the paging
     */
    public String getPaging() {
        return paging;
    }

    /**
     * Sets paging.
     *
     * @param paging the paging
     */
    public void setPaging(String paging) {
        this.paging = paging;
    }

    /**
     * Gets param type.
     *
     * @return the param type
     */
    public ParamTypeEnum getParamType() {
        return paramType;
    }

    /**
     * Sets param type.
     *
     * @param paramType the param type
     */
    public void setParamType(ParamTypeEnum paramType) {
        this.paramType = paramType;
    }

    /**
     * Gets multiplicity.
     *
     * @return the multiplicity
     */
    public MultiplicityEnum getMultiplicity() {
        return multiplicity;
    }

    /**
     * Sets multiplicity.
     *
     * @param multiplicity the multiplicity
     */
    public void setMultiplicity(MultiplicityEnum multiplicity) {
        this.multiplicity = multiplicity;
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
     * Gets resulttype.
     *
     * @return the resulttype
     */
    public String getResulttype() {
        return resulttype;
    }

    /**
     * Sets resulttype.
     *
     * @param resulttype the resulttype
     */
    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }

    /**
     * Gets resultmap.
     *
     * @return the resultmap
     */
    public String getResultmap() {
        return resultmap;
    }

    /**
     * Sets resultmap.
     *
     * @param resultmap the resultmap
     */
    public void setResultmap(String resultmap) {
        this.resultmap = resultmap;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets cdata.
     *
     * @return the cdata
     */
    public String getCdata() {
        return cdata;
    }

    /**
     * Sets cdata.
     *
     * @param cdata the cdata
     */
    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    /**
     * Gets primitive params.
     *
     * @return the primitive params
     */
    public Map<String, String> getPrimitiveParams() {
        return primitiveParams;
    }

    /**
     * Add primitive param.
     *
     * @param attr the attr
     * @param type the type
     */
    public void addPrimitiveParam(String attr, String type) {
        this.primitiveParams.put(attr, type);
    }

    /**
     * Gets primitive foreach params.
     *
     * @return the primitive foreach params
     */
    public Map<String, String> getPrimitiveForeachParams() {
        return primitiveForeachParams;
    }

    /**
     * Add primitive foreach param.
     *
     * @param itemName the item name
     * @param collName the coll name
     */
    public void addPrimitiveForeachParam(String itemName, String collName) {
        if(this.primitiveForeachParams.containsKey(itemName)){
            this.primitiveForeachParams.put(itemName+collName, collName);
        }else {
            this.primitiveForeachParams.put(itemName, collName);
        }
    }

    /**
     * Gets sql desc.
     *
     * @return the sql desc
     */
    public String getSqlDesc() {
        return sqlDesc;
    }

    /**
     * Sets sql desc.
     *
     * @param sqlDesc the sql desc
     */
    public void setSqlDesc(String sqlDesc) {
        this.sqlDesc = sqlDesc;
    }

    /**
     * Gets cdata page count.
     *
     * @return the cdata page count
     */
    public String getCdataPageCount() {
        return cdataPageCount;
    }

    /**
     * Sets cdata page count.
     *
     * @param cdataPageCount the cdata page count
     */
    public void setCdataPageCount(String cdataPageCount) {
        this.cdataPageCount = cdataPageCount;
    }
}
