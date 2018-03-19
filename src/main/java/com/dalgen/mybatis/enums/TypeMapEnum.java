package com.dalgen.mybatis.enums;

import com.dalgen.mybatis.exception.DalgenException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bangis.wangdf on 15/12/5. 字段映射枚举
 */
public enum TypeMapEnum {
    /**
     * Char type map enum.
     */
    CHAR("CHAR", "String"), //    CHAR	String
    /**
     * Varchar type map enum.
     */
    VARCHAR("VARCHAR", "String"), //    VARCHAR	String
    /**
     * Longvarchar type map enum.
     */
    LONGVARCHAR("LONGVARCHAR", "String"), //    LONGVARCHAR	String
    /**
     * Numeric type map enum.
     */
    NUMERIC("NUMERIC", "java.math.BigDecimal"), //    NUMERIC	java.math.BigDecimal
    /**
     * Decimal type map enum.
     */
    DECIMAL("DECIMAL", "java.math.BigDecimal"), //    DECIMAL	java.math.BigDecimal
    /**
     * Bit type map enum.
     */
    BIT("BIT", "Boolean"), //    BIT	Boolean
    /**
     * Tinyint type map enum.
     */
    TINYINT("TINYINT", "Integer"), //    TINYINT	Integer
    /**
     * Smallint type map enum.
     */
    SMALLINT("SMALLINT", "Integer"), //    SMALLINT	Integer

    /**
     * Int type map enum.
     */
    INT("INT", "Integer"), //    INTEGER	Integer

    /**
     * Integer type map enum.
     */
    INTEGER("INTEGER", "Integer"), //    INTEGER	Integer
    /**
     * Bigint type map enum.
     */
    BIGINT("BIGINT", "Long"), //    BIGINT	Long
    /**
     * Real type map enum.
     */
    REAL("REAL", "Float"), //    REAL	Float
    /**
     * Float type map enum.
     */
    FLOAT("FLOAT", "Double"), //    FLOAT	Double
    /**
     * Double type map enum.
     */
    DOUBLE("DOUBLE", "Double"), //    DOUBLE	Double
    /**
     * Binary type map enum.
     */
    BINARY("BINARY", "byte"), //    BINARY	byte
    /**
     * Varbinary type map enum.
     */
    VARBINARY("VARBINARY", "byte"), //    VARBINARY	byte
    /**
     * Longvarbinary type map enum.
     */
    LONGVARBINARY("LONGVARBINARY", "byte"), //    LONGVARBINARY	byte
    /**
     * Date type map enum.
     */
    DATE("DATE", "java.sql.Date"), //    DATE	java.sql.Date
    /**
     * Time type map enum.
     */
    TIME("TIME", "java.sql.Time"), //    TIME	java.sql.Time
    /**
     * Datetime type map enum.
     */
    DATETIME("TIMESTAMP", "java.sql.Time"), //    TIME	java.sql.Time
    /**
     * Timestamp type map enum.
     */
    TIMESTAMP("TIMESTAMP", "java.sql.Timestamp"), //    TIMESTAMP	java.sql.Timestamp

    OTHER("other", "other");

    /**
     * The Jdbc type.
     */
    //
    private String jdbcType;
    /**
     * The Java type.
     */
    private String javaType;

    /**
     * Instantiates a new Type map enum.
     *
     * @param jdbcType the jdbc type
     * @param javaType the java type
     */
    private TypeMapEnum(String jdbcType, String javaType) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    private static Map<String, TypeMapEnum> codeLookup = new HashMap<String, TypeMapEnum>();

    static {
        for (TypeMapEnum type : TypeMapEnum.values()) {
            codeLookup.put(type.name(), type);
        }
    }

    public static TypeMapEnum getByJdbcType(String jdbcType) {
        TypeMapEnum type = codeLookup.get(jdbcType);
        if (type != null) {
            return type;
        }
        throw new DalgenException("类型转换错误:" + jdbcType);
    }

    /**
     * Gets by jdbc type.
     *
     * @param jdbcType the jdbc type
     * @return the by jdbc type
     */
    public static TypeMapEnum getByJdbcTypeWithOther(String jdbcType) {
        TypeMapEnum type = codeLookup.get(jdbcType);
        if (type != null) {
            return type;
        }
        return OTHER;
    }

    /**
     * Gets jdbc type.
     *
     * @return the jdbc type
     */
    public String getJdbcType() {
        return jdbcType;
    }

    /**
     * Gets java type.
     *
     * @return the java type
     */
    public String getJavaType() {
        return javaType;
    }
}
