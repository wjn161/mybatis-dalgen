package com.dalgen.mybatis.model.java;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.List;

/**
 * Created by bangis.wangdf on 15/12/13. Desc
 */
public class Base {
    /**
     * The Class name.
     */
    private String       className;

    /**
     * The Desc.
     */
    private String       desc;

    /**
     * The Package name.
     */
    private String       packageName;

    /**
     * The Class path.
     */
    private String       classPath;

    /**
     * The Table name.
     */
    private String       tableName;

    /**
     * The Import lists.
     */
    private List<String> importLists = Lists.newArrayList();

    /**
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets class path.
     *
     * @return the class path
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Sets class path.
     *
     * @param classPath the class path
     */
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    /**
     * Gets import lists.
     *
     * @return the import lists
     */
    public List<String> getImportLists() {
        return importLists;
    }

    /**
     * Add import list.
     *
     * @param importClass the import class
     */
    public void addImport(String importClass) {
        if (!this.importLists.contains(importClass)) {
            this.importLists.add(importClass);
        }
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets table name.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
