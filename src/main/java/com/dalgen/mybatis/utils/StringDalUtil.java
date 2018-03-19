package com.dalgen.mybatis.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by bangis.wangdf on 15/12/18. Desc
 */
public class StringDalUtil {
    /**
     * Join string.
     *
     * @param p1 the p 1
     * @param p2 the p 2
     * @return the string
     */
    public static String join(String p1, String p2) {
        if (p1 == null && p2 == null) {
            return "";
        }
        String o1 = p1 == null ? "" : p1, o2 = p2 == null ? "" : p2;
        return o1 + " " + o2;
    }

    /**
     * Upper first string.
     *
     * @param str the str
     * @return the string
     */
    public static String upperFirst(String str) {
        return CamelCaseUtils.toCapitalizeCamelCase(CamelCaseUtils.toUnderlineName(str));

    }

    /**
     * Lower first string.
     *
     * @param str the str
     * @return the string
     */
    public static String lowerFirst(String str) {
        return CamelCaseUtils.toCamelCase(CamelCaseUtils.toUnderlineName(str));
    }

}
