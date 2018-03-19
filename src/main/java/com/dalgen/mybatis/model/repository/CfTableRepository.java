package com.dalgen.mybatis.model.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dalgen.mybatis.exception.DalgenException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dalgen.mybatis.enums.MultiplicityEnum;
import com.dalgen.mybatis.enums.ParamTypeEnum;
import com.dalgen.mybatis.model.config.CfColumn;
import com.dalgen.mybatis.model.config.CfOperation;
import com.dalgen.mybatis.model.config.CfResultMap;
import com.dalgen.mybatis.model.config.CfTable;
import com.dalgen.mybatis.utils.CamelCaseUtils;
import com.google.common.collect.Lists;

/**
 * Created by bangis.wangdf on 15/12/6. Desc
 */
public class CfTableRepository {
    /**
     * The constant PARAM_PATTERN.
     */
    private static final Pattern PARAM_PATTERN           = Pattern.compile("#\\{(.*?)\\}");

    /**
     * The constant STAR_BRACKET. 将 select * from 替换的正则
     */
    private static final Pattern STAR_BRACKET            = Pattern.compile("\\((\\s*\\*\\s*)\\)");

    /**
     * 匹配?参数
     */
    private static final Pattern QUESTION_MARK_PATTERN   = Pattern.compile("\\w+\\s*=\\s*\\?");
    /**
     * 从?参数中获取 column参数
     */
    private static final Pattern QUESTION_COLUMN_PATTERN = Pattern.compile("\\w{1,}");

    /**
     * The constant FOR_DESC_SQL_P. 为mapper.java的方法准备注释
     */
    private static final String  FOR_DESC_SQL_P          = "\\s*<.*>\\s*";
    /**
     * The constant FOR_DESC_SQL_PN.
     */
    private static final String  FOR_DESC_SQL_PN         = "\\s{2,}";

    /**
     * The constant ORDER_BY_PATTERN.
     */
    private static final String  ORDER_BY_PATTERN        = "[o|O][r|R][d|D][e|E][r|R]\\s+[b|B][y|Y]\\s+";
    /**
     * The constant SELECT_FROM_PATTERN.
     * 正则表达式,贪婪匹配,勉强匹配  .* 贪婪    .*? 勉强,之匹配最近一个
     */
    private static final Pattern SELECT_FROM_PATTERN            = Pattern.compile("[s|S][e|E][l|L][e|E][c|C][t|T]\\s+[\\s\\S]*?\\s+[f|F][r|R][o|O][m|M]");
    /**
     * Gain cf table cf table.
     *
     * @param tableFile the table file
     * @return the cf table
     * @throws DocumentException the document exception
     */
    public CfTable gainCfTable(File tableFile) throws DocumentException {
        CfTable cfTable = new CfTable();

        SAXReader saxReader = new SAXReader();
        saxReader.setEntityResolver(new IgnoreDTDEntityResolver()); // ignore dtd
        Document document = saxReader.read(tableFile);

        Element table = document.getRootElement();

        cfTable.setSqlname(attr(table, "sqlname"));
        cfTable.setPhysicalName(attr(table, "physicalName"));
        cfTable.setRemark(attr(table, "remark"));
        cfTable.setSequence(attr(table, "sequence"));

        fillColumns(cfTable, table);

        fillResultMap(cfTable, table);

        fillOperation(cfTable, table);

        return cfTable;
    }

    /**
     * Fill result map.
     *
     * @param cfTable the cf table
     * @param table the table
     */
    private void fillResultMap(CfTable cfTable, Element table) {
        List<Element> elements = table.elements("resultmap");
        for (Element e : elements) {
            CfResultMap cfResultMap = new CfResultMap();
            cfResultMap.setName(attr(e, "name"));
            cfResultMap.setType(attr(e, "type"));
            cfResultMap.setRemark(attr(e, "remark"));
            List<Element> ers = e.elements();
            for (Element er : ers) {
                CfColumn cfColumn = new CfColumn();
                cfColumn.setName(attr(er, "name"));
                cfColumn.setJavatype(attr(er, "javatype"));
                cfColumn.setSqlType(attr(er, "jdbctype"));
                cfColumn.setRemark(attr(er, "remark"));
                cfColumn.setRelatedColumn(attr(er, "relatedColumn"));
                cfResultMap.addColumn(cfColumn);
            }
            cfTable.addResultMap(cfResultMap);
        }
    }

    /**
     * Fill operation.
     *
     * @param cfTable the cf table
     * @param table the table
     */
    private void fillOperation(CfTable cfTable, Element table) {
        List<Element> elements = table.elements("operation");
        for (Element e : elements) {
            CfOperation cfOperation = new CfOperation();
            cfOperation.setRemark(attr(e, "remark"));
            cfOperation.setName(attr(e, "name"));
            cfOperation.setMultiplicity(MultiplicityEnum.getByCode(attr(e, "multiplicity")));
            cfOperation.setPaging(attr(e, "paging"));
            if (cfOperation.getMultiplicity() == MultiplicityEnum.paging) {
                Validate.notEmpty(cfOperation.getPaging(), "需要设置paging,用来生成分页类");
            }
            cfOperation.setParamType(ParamTypeEnum.getByCode(attr(e, "paramtype")));
            cfOperation.setResultmap(attr(e, "resultmap"));
            cfOperation.setResulttype(attr(e, "resulttype"));
            cfOperation.setTimeout(attrLong(e, "timeout"));

            //sql内容
            //setCfOperationCdata
            setCfOperationCdata(cfTable, e, cfOperation);

            fillOperationParams(e, cfOperation);

            cfTable.addOperation(cfOperation);
        }
    }

    /**
     * Sets cf operation cdata.
     *
     * @param cfTable the cf table
     * @param e the e
     * @param cfOperation the cf operation
     */
    private void setCfOperationCdata(CfTable cfTable, Element e, CfOperation cfOperation) {
        String cXml = e.asXML();
        String[] lines = StringUtils.split(cXml, "\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.length - 1; i++) {
            if (i > 1) {
                sb.append("\n");
            }
            sb.append(lines[i]);
        }
        String cdata = sb.toString();
        //SQlDESC
        String sqlDesc = cdata.replaceAll(FOR_DESC_SQL_P, " ");
        sqlDesc = sqlDesc.replaceAll(FOR_DESC_SQL_PN, " ");
        cfOperation.setSqlDesc(sqlDesc);

        String text = e.getTextTrim();
        if (StringUtils.indexOf(text, "*") > 0) {
            Matcher m = STAR_BRACKET.matcher(text);
            if (!m.find()) {
                cdata = StringUtils.replace(cdata, "*", "<include refid=\"Base_Column_List\" />");
            }
        }
        //? 参数替换 不指定类型
        cdata = delQuestionMarkParam(cdata, cfOperation, cfTable);

        //添加sql注释,以便于DBA 分析top sql 定位
        cfOperation.setCdata(addSqlAnnotation(cdata, cfOperation.getName(), cfTable.getSqlname()));
        //pageCount添加
        setCfOperationPageCdata(cdata, cfOperation);
    }

    /**
     * ? 参数替换
     * 
     * @param cdata
     * @param cfOperation
     * @param cfTable
     * @return
     */
    private String delQuestionMarkParam(String cdata, CfOperation cfOperation, CfTable cfTable) {
        //
        if (!StringUtils.contains(cdata, '?')) {
            return cdata;
        }
        cfTable.getColumns();
        if (StringUtils.startsWithIgnoreCase(cfOperation.getName(), "insert")) {
            //TODO cdata中 insert ? 参数替换 不指定类型
            String sql = cdata;
            //sql 特殊处理一下
            sql = sql.replaceAll("\\s{1,}", "");
            sql = sql.replaceAll("\\(\\)", "");
            sql = sql.replaceAll("\\(", "\n(\n");
            sql = sql.replaceAll("\\)", "\n)\n");

            String[] sqlLines = StringUtils.split(sql, "\n");

            int i = 0;
            for (String sqlLine : sqlLines) {
                if (StringUtils.startsWith(sqlLine, "(")) {
                    break;
                }
                i++;
            }
            String insertLine = sqlLines[i + 1];
            String valueLine = sqlLines[i + 5];
            valueLine = valueLine.replaceAll("\\w{1},\\w{1}", "");
            String[] columns = StringUtils.split(insertLine, ',');
            String[] params = StringUtils.split(valueLine, ',');

            for (int j = 0; j < params.length; j++) {
                if (StringUtils.equals(params[j], "?")) {
                    try {
                        String columnParam = CamelCaseUtils.toCamelCase(columns[j]);
                        cdata = StringUtils.replace(cdata, "?", "#{" + columnParam + "}", 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new DalgenException("参数设置错误#{}中,未正确使用 table=" + cfTable.getSqlname());
                    }

                }
            }

        } else {
            Matcher questionMarkPatternResult = QUESTION_MARK_PATTERN.matcher(cdata);
            while (questionMarkPatternResult.find()) {
                Matcher columnMatcher = QUESTION_COLUMN_PATTERN.matcher(questionMarkPatternResult
                        .group());
                while (columnMatcher.find()) {
                    String columnParam = CamelCaseUtils.toCamelCase(columnMatcher.group());
                    cdata = StringUtils.replace(cdata, "?", "#{" + columnParam + "}", 1);
                    cfOperation.addPrimitiveParam(columnParam, "");
                }
            }
        }
        return cdata;
    }

    private static final String REPLACE_TMP = " ( ⊙ o ⊙ ) ";

    /**
     * Sets cf operation page cdata.
     *
     * @param cdata the cdata
     * @param cfOperation the cf operation
     */
    private void setCfOperationPageCdata(String cdata, CfOperation cfOperation) {

        if (cfOperation.getMultiplicity() == MultiplicityEnum.paging) {//分页配置

            String forCount = cdata;
            Matcher selectFromMather = SELECT_FROM_PATTERN.matcher(cdata);
            if(selectFromMather.find()){
                forCount = selectFromMather.replaceFirst("SELECT\n          COUNT(*) AS total \n        FROM\n");
            }

            String cdataCount = forCount.replaceAll(ORDER_BY_PATTERN, REPLACE_TMP);
            int indexof = cdataCount.indexOf(REPLACE_TMP);
            if (indexof > 0) {
                cdataCount = cdataCount.substring(0, indexof).replaceAll(
                        "(?m)^\\s*$" + System.lineSeparator(), "");
            }

            cfOperation.setCdataPageCount(cdataCount);

        }
    }

    /**
     * Add sql annotation string.
     *
     * @param cdata the cdata
     * @param oName the o name
     * @param tbName the tb name
     * @return the string
     */
    private String addSqlAnnotation(String cdata, String oName, String tbName) {

        String sqlAnnotation = StringUtils.upperCase(CamelCaseUtils.toInlineName(CamelCaseUtils
                .toCamelCase("ms_" + tbName + "_" + oName)));
        if (StringUtils.startsWithIgnoreCase(oName, "insert ")
                || StringUtils.startsWithIgnoreCase(oName, "update")
                || StringUtils.startsWithIgnoreCase(oName, "delete")) {
            if (StringUtils.contains(cdata, "update ")) {
                return StringUtils.replace(cdata, "update ", "update /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "UPDATE ")) {
                return StringUtils.replace(cdata, "UPDATE ", "UPDATE /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "insert ")) {
                return StringUtils.replace(cdata, "insert ", "insert /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "INSERT ")) {
                return StringUtils.replace(cdata, "INSERT ", "INSERT /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "delete ")) {
                return StringUtils.replace(cdata, "delete ", "delete /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "DELETE ")) {
                return StringUtils.replace(cdata, "DELETE ", "DELETE /*" + sqlAnnotation + "*/ ");
            }
        } else {
            if (StringUtils.contains(cdata, "select ")) {
                return StringUtils.replace(cdata, "select ", "select /*" + sqlAnnotation + "*/ ");
            }
            if (StringUtils.contains(cdata, "SELECT ")) {
                return StringUtils.replace(cdata, "SELECT", "SELECT /*" + sqlAnnotation + "*/ ");
            }
        }

        return cdata;
    }

    /**
     * Fill operation params. 原生态参数获取 添加List参数支持
     *
     * @param e the e
     * @param cfOperation the cf operation
     */
    private void fillOperationParams(Element e, CfOperation cfOperation) {

        if (cfOperation.getParamType() != ParamTypeEnum.primitive) {
            return;
        }

        //取出foreach 用来判断是否有List参数
        List<Element> items = e.elements();

        if (CollectionUtils.isNotEmpty(items)) {
            for (Element item : items) {
                List<Element> ies = item.elements();
                if (CollectionUtils.isNotEmpty(ies)) {
                    for (Element ie : ies) {
                        if (StringUtils.endsWithIgnoreCase(ie.getName(), "foreach")) {
                            String collName = ie.attributeValue("collection");
                            String itemName = ie.attributeValue("item");
                            Validate.notEmpty(collName,
                                    "foreach 元素设置错误 table=" + cfOperation.getName());
                            Validate.notEmpty(itemName,
                                    "foreach 元素设置错误 table=" + cfOperation.getName());
                            cfOperation.addPrimitiveForeachParam(itemName, collName);
                        }
                    }
                } //找到List参数
                else if (StringUtils.endsWithIgnoreCase(item.getName(), "foreach")) {
                    String collName = item.attributeValue("collection");
                    String itemName = item.attributeValue("item");
                    Validate.notEmpty(collName, "foreach 元素设置错误 table=" + cfOperation.getName());
                    Validate.notEmpty(itemName, "foreach 元素设置错误 table=" + cfOperation.getName());
                    cfOperation.addPrimitiveForeachParam(itemName, collName);
                }
            }
        }

        Matcher m = PARAM_PATTERN.matcher(e.asXML());
        List<String> params = Lists.newArrayList();
        while (m.find()) {
            params.add(m.group(1));
        }
        for (String p : params) {
            String attr = null;
            String type = null;
            for (String s : StringUtils.split(p, ",")) {
                if (s.contains("=")) {
                    if (StringUtils.startsWithIgnoreCase(s, "javaType")
                            || StringUtils.startsWithIgnoreCase(s, "jdbcType")) {
                        type = StringUtils.split(s, "=")[1].trim();
                    }
                } else {
                    attr = StringUtils.trim(s);
                }
            }
            cfOperation.addPrimitiveParam(attr, type);
        }
    }

    /**
     * Fill columns.
     *
     * @param cfTable the cf table
     * @param table the table
     */
    private void fillColumns(CfTable cfTable, Element table) {

        List<Element> elements = table.elements("column");
        for (Element e : elements) {
            CfColumn cfColumn = new CfColumn();
            cfColumn.setName(attr(e, "name"));
            cfColumn.setJavatype(attr(e, "javatype"));
            cfColumn.setRelatedColumn(attr(e, "relatedColumn"));
            cfTable.addColumn(cfColumn);
        }

    }

    /**
     * Attr string.
     *
     * @param e the e
     * @param attr the attr
     * @return the string
     */
    private String attr(Element e, String attr) {
        if (e == null || attr == null) {
            return null;
        }
        Attribute attribute = e.attribute(attr);
        if (attribute == null) {
            return null;
        } else {
            return attribute.getText();
        }
    }

    /**
     * Attr string.
     *
     * @param e the e
     * @param attr the attr
     * @return the string
     */
    private Long attrLong(Element e, String attr) {
        if (e == null || attr == null) {
            return null;
        }
        Attribute attribute = e.attribute(attr);
        if (attribute == null) {
            return null;
        } else {
            return Long.valueOf(attribute.getText());
        }
    }
}
