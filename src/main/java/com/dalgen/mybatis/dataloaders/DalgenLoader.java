package com.dalgen.mybatis.dataloaders;

import com.dalgen.mybatis.common.FileNameSelector;
import com.dalgen.mybatis.enums.MultiplicityEnum;
import com.dalgen.mybatis.enums.ParamTypeEnum;
import com.dalgen.mybatis.enums.TypeMapEnum;
import com.dalgen.mybatis.model.Gen;
import com.dalgen.mybatis.model.config.CfColumn;
import com.dalgen.mybatis.model.config.CfOperation;
import com.dalgen.mybatis.model.config.CfResultMap;
import com.dalgen.mybatis.model.config.CfTable;
import com.dalgen.mybatis.model.dbtable.Column;
import com.dalgen.mybatis.model.dbtable.Table;
import com.dalgen.mybatis.model.java.*;
import com.dalgen.mybatis.model.java.domapper.DOMapperMethod;
import com.dalgen.mybatis.model.java.domapper.DOMapperMethodParam;
import com.dalgen.mybatis.model.repository.CfTableRepository;
import com.dalgen.mybatis.model.repository.TableRepository;
import com.dalgen.mybatis.utils.CamelCaseUtils;
import com.dalgen.mybatis.utils.ConfigUtil;
import com.dalgen.mybatis.utils.StringDalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/3. Desc
 */
public class DalgenLoader extends AbstractDalgenLoader {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();
    public static final String LONG = "Long";
    public static final String PRIMARY_KEY_NAME = "ID";
    public static final String PRIMARY_KEY_TYPE = "BIGINT";

    /**
     * The Table repository.
     */
    private TableRepository tableRepository = new TableRepository();

    /**
     * The Gen xml file repository.
     */
    private CfTableRepository cfTableRepository = new CfTableRepository();

    /**
     * The constant RESULT_MANY.
     */
    private static final String RESULT_MANY = "List<{0}>";

    /**
     * Load.
     *
     * @param gen        the gen
     * @param connection the connection
     * @param tablesFile the tables file
     * @throws Exception the exception
     */
    @Override
    public void load(Gen gen, Connection connection, File tablesFile) throws Exception {
        String cmd = ConfigUtil.getCmd();
        //解析所有table.xml(为生成sqlMap.xml做准备)
        Map<String, CfTable> cfTableMap = Maps.newHashMap();
        for (File file : tablesFile.listFiles(new FileNameSelector("xml"))) {
            cfTableMap.put(file2DbName(file), cfTableRepository.gainCfTable(file));
        }

        List<String> needGenTableNames = preNeedGenTableNames(cmd, cfTableMap);

        //获取需要重新生成的表(为重新生成Mapper.xml,DO,Mapper.java 准备)
        Map<String, Table> tableMap = Maps.newHashMap();

        for (String tbName : needGenTableNames) {
            tableMap.put(StringUtils.upperCase(tbName),
                    tableRepository.gainTable(connection, tbName, cfTableMap.get(tbName)));
        }

        //根据需要重新生成的表 准备数据
        for (String tbName : needGenTableNames) {
            CfTable cfTable = cfTableMap.get(tbName);
            Table table = tableMap.get(tbName);
            //准备DO
            DO doClass = preDo(table, cfTable.getColumns());
            gen.addDO(doClass);

            //准备Mapper.xml
            XmlMapper xmlMapper = new XmlMapper();
            //准备resultMap对应的对象
            Map<String, Column> tbColumMap = Maps.newHashMap();
            for (Column column : table.getColumnList()) {
                tbColumMap.put(column.getSqlName(), column);
            }
            Map<String, Filelds> fileldsMap = Maps.newHashMap();
            for (Filelds filelds : doClass.getFieldses()) {
                fileldsMap.put(filelds.getName(), filelds);
            }
            Map<String, ResultMap> resultMaps = Maps.newHashMap();

            //preResultMap
            preResultMap(gen, tbName, cfTable, table, xmlMapper, tbColumMap, fileldsMap, resultMaps);

            //准备Mapper接口
            DOMapper doMapper = preDOMapper(gen, cfTable, table, doClass, resultMaps);
            gen.addDOMapper(doMapper);

            //准备DAO类
            DAO dao = preDAO(gen, cfTable, table, doClass, resultMaps);
            getClassAndImport(dao, doMapper.getPackageName() + "." + doMapper.getClassName());
            dao.setDoMapper(doMapper);
            gen.addDao(dao);
            //准备MapperService

            //###
            xmlMapper.setCfTable(cfTable);
            xmlMapper.setDoClass(doClass);
            xmlMapper.setDoMapper(doMapper);
            xmlMapper.setTable(table);
            gen.addXmlMapper(xmlMapper);

        }

    }

    /**
     * Pre need gen table names list.
     *
     * @param cmd        the cmd
     * @param cfTableMap the cf table map
     * @return the list
     */
    private List<String> preNeedGenTableNames(String cmd, Map<String, CfTable> cfTableMap) {
        List<String> needGenTableNames = Lists.newArrayList();
        if (StringUtils.equals(StringUtils.trim(cmd), "*")) {
            needGenTableNames = Lists.newArrayList(cfTableMap.keySet());
        } else {

            for (String tableName : Lists
                    .newArrayList(StringUtils.split(StringUtils.upperCase(cmd)))) {
                boolean flag = true;
                for (String splitTableSuffix : ConfigUtil.getConfig().getSplitTableSuffixs()) {
                    if (StringUtils.endsWithIgnoreCase(tableName, splitTableSuffix)) {
                        needGenTableNames.add(StringUtils.replace(tableName, splitTableSuffix, ""));
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    needGenTableNames.add(tableName);
                }
            }
        }
        return needGenTableNames;
    }

    /**
     * Pre result map.
     *
     * @param gen        the gen
     * @param tbName     the tb name
     * @param cfTable    the cf table
     * @param table      the table
     * @param xmlMapper  the xml mapper
     * @param tbColumMap the tb colum map
     * @param fileldsMap the filelds map
     * @param resultMaps the result maps
     */
    private void preResultMap(Gen gen, String tbName, CfTable cfTable, Table table,
                              XmlMapper xmlMapper, Map<String, Column> tbColumMap,
                              Map<String, Filelds> fileldsMap, Map<String, ResultMap> resultMaps) {
        for (CfResultMap cfResultMap : cfTable.getResultMaps()) {
            ResultMap resultMap = new ResultMap();
            resultMap.setTableName(table.getSqlName());
            resultMap.setName(cfResultMap.getName());
            resultMap.setType(cfResultMap.getType());
            resultMap.setClassName(cfResultMap.getType());
            resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/resultmap");
            resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".resultmap");
            resultMap.setDesc(cfResultMap.getRemark());

            for (CfColumn cfColumn : cfResultMap.getColumns()) {
                Validate.notEmpty(cfColumn.getName(), tbName
                        + "xml 配置有误 DalgenLoader.preResultMap Gen=" + gen);
                Column column = new Column();
                column.setJavaName(CamelCaseUtils.toCamelCase(cfColumn.getName()));
                if (column.getJavaName().toUpperCase().equals(PRIMARY_KEY_NAME) && column.getSqlType().toUpperCase().equals(PRIMARY_KEY_TYPE)) {
                    column.setJavaType(LONG);
                } else {
                    column.setJavaType(cfColumn.getJavatype());
                }
                column.setSqlName(cfColumn.getName());
                column.setSqlType(cfColumn.getSqlType());
                column.setRemarks(cfColumn.getRemark());
                resultMap.addColumn(column);

                Filelds filelds = new Filelds();
                filelds.setJavaType(getClassAndImport(resultMap, column.getJavaType()));
                filelds.setName(column.getJavaName());
                filelds.setDesc(column.getRemarks());

                resultMap.addFields(filelds);
            }
            resultMaps.put(cfResultMap.getName(), resultMap);
            xmlMapper.addResultMap(resultMap);
            gen.addResultMap(resultMap);
        }
    }

    /**
     * Pre dao dao.
     *
     * @param gen        the gen
     * @param cfTable    the cf table
     * @param table      the table
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @return the dao
     */
    private DAO preDAO(Gen gen, CfTable cfTable, Table table, DO doClass,
                       Map<String, ResultMap> resultMaps) {
        DAO dao = new DAO();
        dao.setClassName(table.getJavaName() + "DAO");
        dao.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".dao");
        dao.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/dao");
        dao.setDesc(cfTable.getRemark());
        dao.setTableName(cfTable.getSqlname());

        Map<String, String> columnTypeMap = Maps.newHashMap();
        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            ////处理ID为BIGINT的时候会被识别为Money类型
            if (column.getJavaName().toUpperCase().equals(PRIMARY_KEY_NAME) && column.getSqlType().toUpperCase().equals(PRIMARY_KEY_TYPE)) {
                columnTypeMap.put(column.getJavaName(), LONG);
            } else {
                columnTypeMap.put(column.getJavaName(), column.getJavaType());
            }
            columnDescMap.put(column.getJavaName(), column.getRemarks());
        }

        for (CfOperation operation : cfTable.getOperations()) {
            preDAOMethod(doClass, resultMaps, dao, operation, columnTypeMap);
        }
        return dao;
    }

    /**
     * Pre dao method.
     *
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @param dao        the dao
     * @param operation  the operation
     * @param columnMap  the column map
     */
    private void preDAOMethod(DO doClass, Map<String, ResultMap> resultMaps, DAO dao,
                              CfOperation operation, Map<String, String> columnMap) {

        DOMapperMethod method = new DOMapperMethod();
        method.setName(operation.getName());
        method.setDesc(operation.getRemark());
        method.setSql(operation.getSqlDesc());

        String resultType;
        if (operation.getMultiplicity() == MultiplicityEnum.paging) {
            Paging paging = new Paging();
            paging.setClassName(StringDalUtil.upperFirst(operation.getPaging()) + "Page");
            paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".paging");
            resultType = getClassAndImport(dao,
                    paging.getPackageName() + "." + paging.getClassName());
            DOMapperMethodParam param = new DOMapperMethodParam(resultType,
                    StringDalUtil.lowerFirst(operation.getPaging()));
            method.setPagingFlag("true");
            method.addParam(param);
        } else {
            preMethodParam(doClass, dao, operation, method, columnMap);
            resultType = operationResultType(doClass, dao, operation, resultMaps);
        }
        method.setReturnClass(resultType);

        dao.addMothed(method);
    }

    /**
     * Pre do mapper do mapper.
     *
     * @param gen        the gen
     * @param cfTable    the cf table
     * @param table      the table
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @return the do mapper
     */
    private DOMapper preDOMapper(Gen gen, CfTable cfTable, Table table, DO doClass,
                                 Map<String, ResultMap> resultMaps) {
        DOMapper doMapper = new DOMapper();
        doMapper.setClassName(doClass.getClassName() + "Mapper");
        doMapper.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".mapper");
        doMapper.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/mapper");
        doMapper.setDesc(cfTable.getRemark());
        doMapper.setTableName(cfTable.getSqlname());

        Map<String, String> columnTypeMap = Maps.newHashMap();
        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            if (column.getJavaName().toUpperCase().equals(PRIMARY_KEY_NAME) && column.getSqlType().toUpperCase().equals(PRIMARY_KEY_TYPE)) {
                columnTypeMap.put(column.getJavaName(), LONG);
            } else {
                columnTypeMap.put(column.getJavaName(), column.getJavaType());
            }
        }

        for (CfOperation operation : cfTable.getOperations()) {
            if (operation.getMultiplicity() == MultiplicityEnum.paging) {//分页
                prePagingMethod(gen, cfTable, table, doClass, resultMaps, doMapper, columnTypeMap,
                        columnDescMap, operation);
            } else {
                preMethod(doClass, resultMaps, doMapper, operation, columnTypeMap);
            }
        }
        return doMapper;
    }

    /**
     * Pre paging method.
     *
     * @param gen           the gen
     * @param cfTable       the cf table
     * @param table         the table
     * @param doClass       the do class
     * @param resultMaps    the result maps
     * @param doMapper      the do mapper
     * @param columnTypeMap the column type map
     * @param columnDescMap the column desc map
     * @param operation     the operation
     */
    private void prePagingMethod(Gen gen, CfTable cfTable, Table table, DO doClass,
                                 Map<String, ResultMap> resultMaps, DOMapper doMapper,
                                 Map<String, String> columnTypeMap,
                                 Map<String, String> columnDescMap, CfOperation operation) {
        DOMapperMethod pagingResultMethod = new DOMapperMethod();
        pagingResultMethod.setName(operation.getName() + "Result");
        pagingResultMethod.setPagingName(operation.getName());
        pagingResultMethod.setDesc(operation.getRemark());
        pagingResultMethod.setSql(operation.getSqlDesc());
        pagingResultMethod.setPagingFlag("true");

        Paging paging = new Paging();
        paging.setClassName(StringDalUtil.upperFirst(operation.getPaging()) + "Page");
        paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".paging");
        paging.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/paging");
        paging.setBasePackageName(ConfigUtil.getCurrentDb().getGenDalCommonPackage() + ".paging");
        paging.setBaseClassPath(ConfigUtil.getCurrentDb().getGenDalCommonPackagePath() + "/paging");
        getClassAndImport(paging, paging.getBasePackageName() + ".BasePage");
        paging.setDesc(StringDalUtil.join(table.getSqlName(), cfTable.getRemark()));
        paging.setTableName(cfTable.getSqlname());

        String pagingResultType = operationResultType(doClass, paging, operation, resultMaps);

        paging.setResultType(pagingResultType);
        List<DOMapperMethodParam> params = preMethodParams(paging, operation, columnTypeMap);
        for (DOMapperMethodParam param : params) {
            Filelds filelds = new Filelds();
            filelds.setName(param.getParam());
            filelds.setJavaType(param.getParamType());
            filelds.setDesc(columnDescMap.get(param.getParam()));
            paging.addFields(filelds);
        }
        gen.addPaging(paging);
        //paging import到doMapper
        getClassAndImport(doMapper, paging.getPackageName() + "." + paging.getClassName());
        getClassAndImport(doMapper, "java.util.List");
        //方法返回结果

        DOMapperMethodParam pagingParam = new DOMapperMethodParam(paging.getClassName(),
                StringDalUtil.lowerFirst(operation.getPaging()));
        pagingResultMethod.addParam(pagingParam);

        String resultType = operationResultType(doClass, doMapper, operation, resultMaps);

        paging.setResultType(resultType);
        pagingResultMethod.setReturnClass("List<" + resultType + ">");
        try {
            DOMapperMethod pagingCountMethod = (DOMapperMethod) BeanUtils
                    .cloneBean(pagingResultMethod);
            pagingCountMethod.setName(operation.getName() + "Count");
            pagingCountMethod.setReturnClass("int");
            doMapper.addMothed(pagingCountMethod);
        } catch (Exception e) {
            LOG.error("", e);
        }

        doMapper.addMothed(pagingResultMethod);
    }

    /**
     * Pre method.
     *
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @param doMapper   the do mapper
     * @param operation  the operation
     * @param columnMap  the column map
     */
    private void preMethod(DO doClass, Map<String, ResultMap> resultMaps, DOMapper doMapper,
                           CfOperation operation, Map<String, String> columnMap) {
        DOMapperMethod method = new DOMapperMethod();
        method.setName(operation.getName());
        method.setDesc(operation.getRemark());
        method.setSql(operation.getSqlDesc());
        preMethodParam(doClass, doMapper, operation, method, columnMap);
        String resultType = operationResultType(doClass, doMapper, operation, resultMaps);
        method.setReturnClass(resultType);
        doMapper.addMothed(method);
    }

    /**
     * Pre method param.
     *
     * @param doClass   the do class
     * @param doMapper  the do mapper
     * @param operation the operation
     * @param method    the method
     * @param columnMap the column map
     */
    private void preMethodParam(DO doClass, Base doMapper, CfOperation operation,
                                DOMapperMethod method, Map<String, String> columnMap) {

        if (operation.getParamType() == ParamTypeEnum.object) {
            method.addParam(new DOMapperMethodParam(getClassAndImport(doMapper,
                    doClass.getPackageName() + "." + doClass.getClassName()), "entity"));
        } else {
            method.setParams(preMethodParams(doMapper, operation, columnMap));
        }
    }

    /**
     * Pre method params list.
     *
     * @param doMapper  the do mapper
     * @param operation the operation
     * @param columnMap the column map
     * @return the list
     */
    private List<DOMapperMethodParam> preMethodParams(Base doMapper, CfOperation operation,
                                                      Map<String, String> columnMap) {
        List<DOMapperMethodParam> params = Lists.newArrayList();
        for (Map.Entry pm : operation.getPrimitiveParams().entrySet()) {
            String pmName = (String) pm.getKey();
            String pmType = (String) pm.getValue();
            //如果是DO中的属性 则不需要在处理
            String columnType = columnMap.get(pmName);

            TypeMapEnum typeMapEnum = TypeMapEnum.getByJdbcTypeWithOther(pmType);

            String paramValType = StringUtils.isBlank(columnType) ? (typeMapEnum == TypeMapEnum.OTHER ? pmType
                    : typeMapEnum.getJavaType())
                    : columnType;
            String custJavaType = ConfigUtil.getConfig().getTypeMap().get(paramValType);

            String paramType = getClassAndImport(doMapper, custJavaType == null ? paramValType
                    : custJavaType);

            String foreachName = operation.getPrimitiveForeachParams().get(pmName);
            DOMapperMethodParam methodParam;
            if (StringUtils.isBlank(foreachName)) {
                ////处理ID为BIGINT的时候会被识别为Money类型
                if (pmName.toLowerCase().equals("id") && pmType.toUpperCase().equals("BIGINT")) {
                    methodParam = new DOMapperMethodParam("Long", pmName);
                } else {
                    methodParam = new DOMapperMethodParam(paramType, pmName);
                }
            } else {
                getClassAndImport(doMapper, "java.util.List");
                methodParam = new DOMapperMethodParam("List<" + paramType + ">", foreachName);
            }
            params.add(methodParam);
        }
        return params;
    }

    /**
     * Operation result type string.
     *
     * @param doClass    the do class
     * @param base       the do mapper
     * @param operation  the operation
     * @param resultMaps the result maps
     * @return the string
     */
    private String operationResultType(DO doClass, Base base, CfOperation operation,
                                       Map<String, ResultMap> resultMaps) {

        if (StringUtils.startsWithIgnoreCase(operation.getName(), "insert")
                || StringUtils.startsWithIgnoreCase(operation.getName(), "update")
                || StringUtils.startsWithIgnoreCase(operation.getName(), "delete")) {
            return "Long";
        }
        //返回类不为null
        String resultType;
        if (!StringUtils.isBlank(operation.getResulttype())) {
            resultType = getClassAndImport(base, operation.getResulttype());
        } else if (!StringUtils.isBlank(operation.getResultmap())) {
            ResultMap resultMap = resultMaps.get(operation.getResultmap());
            Validate.notNull(resultMap, "DalgenLoader.operationResultType 自定义ResultMap出错 table = "
                    + doClass.getTableName() + " DO=" + doClass);
            resultType = getClassAndImport(base,
                    resultMap.getPackageName() + "." + resultMap.getClassName());
        } else {
            resultType = getClassAndImport(base,
                    doClass.getPackageName() + "." + doClass.getClassName());
        }

        //返回一行
        if (MultiplicityEnum.many == operation.getMultiplicity()) {
            base.addImport("java.util.List");
            return MessageFormat.format(RESULT_MANY, resultType);
        }
        return resultType;
    }

    /**
     * Pre do do.
     *
     * @param table     the table
     * @param cfColumns the cf columns
     * @return the do
     */
    private DO preDo(Table table, List<CfColumn> cfColumns) {
        DO doClass = new DO();
        doClass.setClassName(table.getJavaName() + "DO");
        doClass.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + ".dataobject");
        doClass.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/dataobject");
        doClass.setDesc(table.getRemark());

        //不在DO中输出地字段
        List<String> rldcList = Lists.newArrayList();
        for (CfColumn cfColumn : cfColumns) {
            if (!StringUtils.isBlank(cfColumn.getRelatedColumn())) {
                rldcList.add(cfColumn.getRelatedColumn());
            }
        }

        for (Column column : table.getColumnList()) {
            //提出不需要在DO中出现的字段
            if (!rldcList.contains(column.getSqlName())) {
                Filelds filelds = new Filelds();
                filelds.setName(column.getJavaName());
                filelds.setDesc(column.getRemarks());
                //处理ID为BIGINT的时候会被识别为Money类型
                if (column.getJavaName().toUpperCase().equals("ID") && column.getSqlType().toUpperCase().equals("BIGINT")) {
                    filelds.setJavaType("Long");
                } else {
                    filelds.setJavaType(getClassAndImport(doClass, column.getJavaType()));
                }
                doClass.addFields(filelds);
            }
        }
        return doClass;
    }

    /**
     * Gets class and import.
     *
     * @param base      the base
     * @param classType the class type
     * @return the class and import
     */
    private String getClassAndImport(Base base, String classType) {
        Validate.notEmpty(classType,
                "DalgenLoader.getClassAndImport error classType 不能为 null Base=" + base);
        int lastIdx = StringUtils.lastIndexOf(classType, ".");
        if (lastIdx > 0) {
            base.addImport(classType);
        }
        //返回方法
        return StringUtils.substring(classType, lastIdx + 1);
    }

}
