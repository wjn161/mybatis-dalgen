package com.dalgen.mybatis.utils;

/**
 * Created by bangis.wangdf on 15/12/5. Desc 驼峰命名法
 */
public class CamelCaseUtils {
    /**
     * The constant SEPARATOR.
     */
    private static final char SEPARATOR = '_';

    /**
     * The constant SEPARATOR.
     */
    private static final char IN_LINE = '-';

    /**
     * To underline name string.
     *
     * @param s the s
     * @return the string
     */
    public static String tolineName(String s,char sp) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0)
                        sb.append(sp);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * To underline name string.
     *
     * @param s the s
     * @return the string
     */
    public static String toUnderlineName(String s) {
        return tolineName(s, SEPARATOR);
    }

    /**
     * To Inline name string.
     *
     * @param s the s
     * @return the string
     */
    public static String toInlineName(String s) {
        return tolineName(s,IN_LINE);
    }
    /**
     * To camel case string.
     *
     * @param s the s
     * @return the string
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * To capitalize camel case string.
     *
     * @param s the s
     * @return the string
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

            public static void main(String[] args) {
                System.out.println(CamelCaseUtils.toInlineName("ISOCertifiedStaff"));
                System.out.println(CamelCaseUtils.toUnderlineName("ISOCertifiedStaff"));
                System.out.println(CamelCaseUtils.toUnderlineName("CertifiedStaff"));
                System.out.println(CamelCaseUtils.toUnderlineName("UserID"));
                System.out.println(CamelCaseUtils.toCamelCase("iso_certified_staff"));
                System.out.println(CamelCaseUtils.toCamelCase("certified_staff"));
                System.out.println(CamelCaseUtils.toCamelCase("user_id"));
                System.out.println(CamelCaseUtils.toCapitalizeCamelCase("user_id"));
            }

}
