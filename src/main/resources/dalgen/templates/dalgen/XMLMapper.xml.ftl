<@pp.dropOutputFile />
<#import "../lib/lib.ftl" as lib/>
<#list dalgen.xmlMappers as xmlMapper>
<@pp.changeOutputFile name = "/main/resources/${xmlMapper.doMapper.classPath}/${xmlMapper.doMapper.className}.xml" />
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${xmlMapper.doMapper.packageName}.${xmlMapper.doMapper.className}">

    <resultMap id="BaseResultMap"  type="${xmlMapper.doClass.packageName}.${xmlMapper.doClass.className}">
<#list xmlMapper.table.columnList as column>
        <#if column.sqlName =="ID"><id column="${column.sqlName}" property="${column.javaName}" jdbcType="${column.sqlType}" javaType="${column.javaType}"/><#else><result column="${column.sqlName}" property="${column.javaName}" jdbcType="${column.sqlType}" javaType="${column.javaType}"/></#if>
</#list>
    </resultMap>

<#list xmlMapper.resultMaps as resultMap>
    <resultMap id="${resultMap.name}"  type="${resultMap.packageName}.${resultMap.className}">
    <#list resultMap.columnList as column>
        <#if column.sqlName =="ID"><id column="${column.sqlName}" property="${column.javaName}"  javaType="${column.javaType}"/><#else><result column="${column.sqlName}" property="${column.javaName}"  javaType="${column.javaType}"/></#if>
    </#list>
    </resultMap>
</#list>

    <#-- baseSql -->
    <sql id="Base_Column_List">
        <#list xmlMapper.table.columnList as column><#if column_index gt 0>,</#if>${column.sqlName}</#list>
    </sql>

    <#-- sql -->
    <#list xmlMapper.cfTable.operations as operation>

    <#if operation.multiplicity.code=="paging"><#--paging-->
    <!--${operation.remark!operation.name} pageCount-->
    <${lib.operation2Sql(operation.name)} id="${operation.name}Count"  resultType="int"${lib.timeout(operation)}>
${operation.cdataPageCount!}
    </${lib.operation2Sql(operation.name)}>
    <!--${operation.remark!operation.name} pageResult-->
    <${lib.operation2Sql(operation.name)} id="${operation.name}Result"  ${lib.mapperResult(operation)}${lib.timeout(operation)}>
${operation.cdata!}
        limit ${"#"}{startRow},${"#"}{limit}
    </${lib.operation2Sql(operation.name)}>
    <#else><#--not paging-->
    <!--${operation.remark!operation.name}-->
    <${lib.operation2Sql(operation.name)} id="${operation.name}" ${lib.mapperResult(operation)}${lib.timeout(operation)}>
${operation.cdata!}
    </${lib.operation2Sql(operation.name)}>
    </#if>
    </#list>
</mapper>
</#list>