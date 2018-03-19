package com.dalgen.mybatis.model;

import java.util.List;

import com.dalgen.mybatis.model.java.*;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.dalgen.mybatis.model.dbtable.Table;
import com.google.common.collect.Lists;

/**
 * Created by bangis.wangdf on 15/12/12. Desc
 */
public class Gen {
    /**
     * table放在哪个目录中,默认是${dbName}Tables The Tables path.
     */
    private String          tablesPath;

    /**
     * The Dalgen root.
     */
    private String          dalgenRoot;

    /**
     * The Out root.
     */
    private String          outRoot;

    /**
     * The Database name.
     */
    private String          dataBaseName;

    /**
     * The Db type.
     */
    private String          dbType;

    /**
     * The Tables.
     */
    private List<Table>     tables     = Lists.newArrayList();

    /**
     * The Result maps.
     */
    private List<ResultMap> resultMaps = Lists.newArrayList();

    /**
     * The Dos.
     */
    private List<DO>        dos        = Lists.newArrayList();

    /**
     * The Daos.
     */
    private List<DAO>       daos       = Lists.newArrayList();

    /**
     * The Do mappers.
     */
    private List<DOMapper>  doMappers  = Lists.newArrayList();

    /**
     * The Xml mappers.
     */
    private List<XmlMapper> xmlMappers = Lists.newArrayList();

    /**
     * The Pagings.
     */
    private List<Paging>    pagings    = Lists.newArrayList();

    /**
     * Gets tables path.
     *
     * @return the tables path
     */
    public String getTablesPath() {
        return tablesPath;
    }

    /**
     * Sets tables path.
     *
     * @param tablesPath the tables path
     */
    public void setTablesPath(String tablesPath) {
        this.tablesPath = tablesPath;
    }

    /**
     * Gets dalgen root.
     *
     * @return the dalgen root
     */
    public String getDalgenRoot() {
        return dalgenRoot;
    }

    /**
     * Sets dalgen root.
     *
     * @param dalgenRoot the dalgen root
     */
    public void setDalgenRoot(String dalgenRoot) {
        this.dalgenRoot = dalgenRoot;
    }

    /**
     * Gets data base name.
     *
     * @return the data base name
     */
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * Sets data base name.
     *
     * @param dataBaseName the data base name
     */
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    /**
     * Gets tables.
     *
     * @return the tables
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * Add table.
     *
     * @param table the table
     */
    public void addTable(Table table) {
        this.tables.add(table);
    }

    /**
     * Gets db type.
     *
     * @return the db type
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Sets db type.
     *
     * @param dbType the db type
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Gets dos.
     *
     * @return the dos
     */
    public List<DO> getDos() {
        return dos;
    }

    /**
     * Add do.
     *
     * @param doClass the do class
     */
    public void addDO(DO doClass) {
        this.dos.add(doClass);
    }

    /**
     * Gets daos.
     *
     * @return the daos
     */
    public List<DAO> getDaos() {
        return daos;
    }

    /**
     * Add dao.
     *
     * @param dao the dao
     */
    public void addDao(DAO dao) {
        this.daos.add(dao);
    }

    /**
     * Gets out root.
     *
     * @return the out root
     */
    public String getOutRoot() {
        return outRoot;
    }

    /**
     * Sets out root.
     *
     * @param outRoot the out root
     */
    public void setOutRoot(String outRoot) {
        this.outRoot = outRoot;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Gets do mappers.
     *
     * @return the do mappers
     */
    public List<DOMapper> getDoMappers() {
        return doMappers;
    }

    /**
     * Add do mapper.
     *
     * @param doMapper the do mapper
     */
    public void addDOMapper(DOMapper doMapper) {
        this.doMappers.add(doMapper);
    }

    /**
     * Gets xml mappers.
     *
     * @return the xml mappers
     */
    public List<XmlMapper> getXmlMappers() {
        return xmlMappers;
    }

    /**
     * Add xml mapper.
     *
     * @param xmlMapper the xml mapper
     */
    public void addXmlMapper(XmlMapper xmlMapper) {
        this.xmlMappers.add(xmlMapper);
    }

    /**
     * Gets result maps.
     *
     * @return the result maps
     */
    public List<ResultMap> getResultMaps() {
        return resultMaps;
    }

    /**
     * Add result map.
     *
     * @param resultMap the result map
     */
    public void addResultMap(ResultMap resultMap) {
        this.resultMaps.add(resultMap);
    }

    /**
     * Gets pagings.
     *
     * @return the pagings
     */
    public List<Paging> getPagings() {
        return pagings;
    }

    /**
     * Add paging.
     *
     * @param paging the paging
     */
    public void addPaging(Paging paging) {
        this.pagings.add(paging);
    }


}
