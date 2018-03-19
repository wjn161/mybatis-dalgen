package com.dalgen.mybatis.common;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件类型选择去
 * <p/>
 *
 * @author bangis.wangdf
 * @date 16/3/23.18:19
 */
public class FileNameSelector implements FilenameFilter {
    private String extension = ".";

    public FileNameSelector(String fileExtension) {
        if (StringUtils.startsWith(fileExtension, extension)) {
            extension += fileExtension;
        } else {
            extension = fileExtension;
        }
    }

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir the directory in which the file was found.
     * @param name the name of the file.
     * @return <code>true</code> if and only if the name should be included in
     *         the file list; <code>false</code> otherwise.
     */
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
