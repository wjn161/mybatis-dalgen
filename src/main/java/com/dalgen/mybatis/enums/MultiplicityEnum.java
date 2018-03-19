package com.dalgen.mybatis.enums;

import com.dalgen.mybatis.exception.DalgenException;
import org.apache.commons.lang.StringUtils;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public enum MultiplicityEnum {
    /**
     * One multiplicity enum.
     */
    one("one"),
    /**
     * Many multiplicity enum.
     */
    many("many"),
    /**
     * Paging multiplicity enum.
     */
    paging("paging");

    /**
     * The Code.
     */
    private String code;

    /**
     * Instantiates a new Multiplicity enum.
     *
     * @param code the code
     */
    private MultiplicityEnum(String code) {
        this.code = code;
    }

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static MultiplicityEnum getByCode(String code) {
        for (MultiplicityEnum multiplicityEnum : MultiplicityEnum.values()) {
            if (StringUtils.equals(code, multiplicityEnum.code)) {
                return multiplicityEnum;
            }
        }
        return MultiplicityEnum.one;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
