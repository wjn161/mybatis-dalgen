package com.dalgen.mybatis.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.dalgen.mybatis.DalgenMojo;

/**
 * Created by bangis.wangdf on 15/12/17. Desc 初始化所需文件<br/>
 * 首次执行自动copy所需文件<br/>
 * 如config.xml已存在则更新templates,table-config-1.1.dtd
 */
public class ConfInit {
    /**
     * The constant BASE_PATH.
     */
    public static final String  BASE_PATH           = ConfInit.class
                                                            .getResource("")
                                                            .getPath()
                                                            .replace(
                                                                    CmdUtil.class.getPackage()
                                                                            .getName()
                                                                            .replace(".", "/")
                                                                            + "/", "");
    private static final String NEED_COPY_CONFIG    = "dalgen/config/";
    private static final String NEED_COPY_TEMPLATES = "dalgen/templates/";

    private static DalgenMojo   dalgenMojo;

    public static void configInit(DalgenMojo dalgenMojo) throws MojoExecutionException,
            MojoFailureException {
        ConfInit.dalgenMojo = dalgenMojo;
        try {
            JarFile jarFile = new JarFile(ConfInit.class.getProtectionDomain().getCodeSource()
                    .getLocation().getPath());
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                //复制模板
                if (StringUtils.startsWithIgnoreCase(jarEntry.getName(), NEED_COPY_TEMPLATES)) {
                    copyAndOverWriteFile(jarEntry.getName(),
                            new File(ConfInit.dalgenMojo.getTemplateDirectory().getAbsolutePath()
                                    + jarEntry.getName()
                                            .substring(NEED_COPY_TEMPLATES.length() - 1)));
                } else if (StringUtils.startsWithIgnoreCase(jarEntry.getName(), NEED_COPY_CONFIG)) {//复制配置文件
                    copyDalgenConfig(jarEntry);
                }

            }
        } catch (IOException e) {
            throw new MojoExecutionException("获取配置信息失败!请联系作者~~~~旺旺:bangis",e);
        }

    }

    /**
     * Copy dalgen config.
     *
     * @param jarEntry the jar entry
     * @throws IOException the io exception
     */
    private static void copyDalgenConfig(JarEntry jarEntry) throws IOException {
        //不是这个开头的直接返回
        if (!StringUtils.startsWithIgnoreCase(jarEntry.getName(), NEED_COPY_CONFIG)) {
            return;
        }
        //这个下面的进行copy
        if (!StringUtils.equalsIgnoreCase(jarEntry.getName(), NEED_COPY_CONFIG)) {
            if (StringUtils.equalsIgnoreCase(jarEntry.getName(), NEED_COPY_CONFIG + "config.xml")) {
                if (!ConfInit.dalgenMojo.getConfig().exists()) {
                    //第一次初始化 提醒用户修改数据源配置
                    copyAndOverWriteFile(jarEntry.getName(), ConfInit.dalgenMojo.getConfig());
                    System.out.println("初始化完成,下一步到 dalgen/config/config.xml配置数据源");
                    System.exit(0);
                }
            } else {//覆盖内容
                copyAndOverWriteFile(jarEntry.getName(), new File(ConfInit.dalgenMojo.getConfig()
                        .getParent() + jarEntry.getName().substring(NEED_COPY_CONFIG.length() - 1)));
            }

        }
    }

    /**
     * Copy and over write file.
     *
     * @param soureName the soure name
     * @param outFile the out file
     * @throws IOException the io exception
     */
    private static void copyAndOverWriteFile(String soureName, File outFile) throws IOException {
        //目录不存在则创建
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        //不是文件则不copy 判断标准为文件含 点 号
        if (StringUtils.indexOf(soureName, '.') == -1) {
            return;
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    ConfInit.class.getResourceAsStream("/" + soureName)));
            writer = new BufferedWriter(new FileWriter(outFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
        } catch (NullPointerException e) {
            System.out.println("======");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

}
