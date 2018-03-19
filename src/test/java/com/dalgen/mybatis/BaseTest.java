package com.dalgen.mybatis;

import java.io.File;
import java.io.IOException;

import com.dalgen.mybatis.utils.CmdUtil;
import junit.framework.TestCase;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;

import com.dalgen.mybatis.utils.ConfigUtil;

/**
 * Created by bangis.wangdf on 15/12/5. Desc
 */
public class BaseTest extends TestCase {
    /**
     * The constant LOG.
     */
    private static final Log   LOG               = new SystemStreamLog();

    /**
     * The constant BASE_PATH.
     */
    public static final String BASE_PATH         = CmdUtil.class
                                                         .getResource("")
                                                         .getPath()
                                                         .replace(
                                                                 CmdUtil.class.getPackage()
                                                                         .getName()
                                                                         .replace(".", "/")
                                                                         + "/", "");

    /**
     * The constant outputDirectory.
     */
    public static File         outputDirectory   = new File(BASE_PATH + "out/");

    /**
     * The constant templateDirectory.
     */
    public static File         templateDirectory = new File(BASE_PATH + "dalgen/templates/");

    /**
     * The constant config.
     */
    public static File         config            = new File(BASE_PATH + "dalgen/config/config.xml");

    static {

        try {
            ConfigUtil.readConfig(config);
            ConfigUtil.setCmd("ws_user_info");
            ConfigUtil.setCurrentDb("westockcore");
            //ConfigUtil.setCmd("dc_per_account_dept_role");
           // ConfigUtil.setCmd("fp_oac_ast_attr_000");
           // ConfigUtil.setCmd("dc_hello");
            //ConfigUtil.setCmd("*");
        } catch (IOException e) {
            LOG.error(e);
        }
    }

}
