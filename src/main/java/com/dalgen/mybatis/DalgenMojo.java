package com.dalgen.mybatis;

import java.io.File;
import java.io.IOException;

import com.dalgen.mybatis.utils.ConfInit;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.dalgen.mybatis.dataloaders.DalgenLoader;
import com.dalgen.mybatis.dataloaders.DalgenTbLoader;
import com.dalgen.mybatis.utils.CmdUtil;
import com.dalgen.mybatis.utils.ConfigUtil;

import fmpp.ProcessingException;
import fmpp.progresslisteners.ConsoleProgressListener;
import fmpp.setting.SettingException;
import fmpp.setting.Settings;
import fmpp.tdd.EvalException;
import fmpp.tdd.Interpreter;
import fmpp.util.MiscUtil;

/**
 * Created by bangis.wangdf on 15/12/3. Desc mybatis 代码生成器
 *
 * @goal gen
 * @phase generate -sources
 */
public class DalgenMojo extends AbstractMojo {

    /**
     * The constant DEFAULT_ERROR_MSG.
     */
    private static final String DEFAULT_ERROR_MSG = "\"%s\" is a required parameter. ";

    /**
     * The constant cmdUtil.
     */
    private CmdUtil             cmdUtil           = new CmdUtil();

    /**
     * Project instance, needed for attaching the build info file. Used to add
     * new source directory to the build.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     * @since 1.0
     */
    private MavenProject        project;

    /**
     * Location of the output files.
     *
     * @parameter default-value="src/"
     */
    private File                outputDirectory;

    /**
     * Location of the FreeMarker template files.
     * ${project.build.directory}/generated-sources/fmpp/
     *
     * @parameter default-value="${project.build.outputDirectory}/templates/"
     * @since 1.0
     */
    private File                templateDirectory;

    /**
     * 配置文件
     *
     * @parameter default-value="dalgen/config/config.xml"
     */
    private File                config;

    /**
     * copyTemplate
     *
     * @parameter default-value=true
     */
    private boolean             copyTemplate;

    private boolean  testF = false;

    /**
     * Instantiates a new Dalgen mojo.
     */
    public DalgenMojo() {
        super();
    }

    /**
     * Instantiates a new Dalgen mojo. for Test
     *
     * @param outputDirectory the output directory
     * @param templateDirectory the template directory
     * @param config the config
     * @param project the project
     */
    public DalgenMojo(File outputDirectory, File templateDirectory, File config,
                      MavenProject project,boolean testF) {
        this.outputDirectory = outputDirectory;
        this.templateDirectory = templateDirectory;
        this.config = config;
        this.project =project;
        this.testF =testF;
    }

    /**
     * Execute.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        configInit(false);

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try {
            ConfigUtil.readConfig(config);
            String _cmd = cmdUtil.consoleInput();
            if ("q".equals(_cmd)) {
                getLog().info("alidalgen 放弃生成");
                return;
            }
            ConfigUtil.dalgenPath = config.getParentFile().getParent();
            executeInit();
            executeGen();
        } catch (Exception e) {
            getLog().error(MiscUtil.causeMessages(e));
            throw new MojoFailureException(MiscUtil.causeMessages(e), e);
        } finally {

        }
    }

    /**
     * Config init.
     *
     * @throws MojoExecutionException the mojo execution exception
     * @throws MojoFailureException the mojo failure exception
     */
    private void configInit(boolean testF) throws MojoExecutionException, MojoFailureException {
        if(testF){
            return;
        }
        if (project == null) {
            throw new MojoExecutionException(String.format(DEFAULT_ERROR_MSG, "project")
                    + "This plugin can be used only inside a project.");
        }

        //复制模板
        if(this.copyTemplate) {
            getLog().info("初始化配置信息开始");
            ConfInit.configInit(this);
            getLog().info("初始化配置信息开始结束 - 请在dalgen/config/config.xml 中配置数据源");
        }

    }

    /**
     * Execute init.
     *
     * @throws SettingException the setting exception
     * @throws IOException the io exception
     * @throws EvalException the eval exception
     * @throws ProcessingException the processing exception
     */
    private void executeInit() throws SettingException, IOException, EvalException,
            ProcessingException {
        Settings settings = new Settings(new File("."));
        settings.set(Settings.NAME_SOURCE_ROOT, templateDirectory.getAbsolutePath());
        settings.set(Settings.NAME_OUTPUT_ROOT, config.getParentFile().getParent());
        settings.set(Settings.NAME_OUTPUT_ENCODING, "UTF-8");
        settings.set(Settings.NAME_SOURCE_ENCODING, "UTF-8");

        //输入项目

        settings.set(Settings.NAME_DATA, "dalgen: " + DalgenTbLoader.class.getName()
                + "(),project:1");
        settings.set(Settings.NAME_MODES, Interpreter.evalAsSequence("ignore(*/config/*.*),"
                + "ignore(lib/*.*)," + "ignore(css/*.*),ignore(dalgen/*.*)"));

        settings.addProgressListener(new ConsoleProgressListener());
        settings.execute();

        getLog().info("初始化表完成");
    }

    /**
     * Execute gen.
     *
     * @throws SettingException the setting exception
     * @throws IOException the io exception
     * @throws EvalException the eval exception
     * @throws ProcessingException the processing exception
     */
    private void executeGen() throws SettingException, IOException, EvalException,
            ProcessingException {
        Settings settings = new Settings(new File("."));
        settings.set(Settings.NAME_SOURCE_ROOT, templateDirectory.getAbsolutePath());
        settings.set(Settings.NAME_OUTPUT_ROOT, outputDirectory.getAbsolutePath());
        settings.set(Settings.NAME_OUTPUT_ENCODING, "UTF-8");
        settings.set(Settings.NAME_SOURCE_ENCODING, "UTF-8");

        //输入项目
        settings.set(Settings.NAME_DATA, "dalgen: " + DalgenLoader.class.getName() + "()");
        settings.set(Settings.NAME_MODES, Interpreter.evalAsSequence("ignore(config/*.*),"
                + "ignore(lib/*.*)," + "ignore(css/*.*),ignore(init/*.*)"));

        settings.addProgressListener(new ConsoleProgressListener());
        settings.execute();

        getLog().info("alidalgen 成功生成");
    }

    /**
     * Sets cmd util. forTest
     *
     * @param cmdUtil the cmd util
     */
    public void setCmdUtil(CmdUtil cmdUtil) {
        this.cmdUtil = cmdUtil;
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * Gets output directory.
     *
     * @return the output directory
     */
    public File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Gets template directory.
     *
     * @return the template directory
     */
    public File getTemplateDirectory() {
        return templateDirectory;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public File getConfig() {
        return config;
    }

    /**
     * Is copy template boolean.
     *
     * @return the boolean
     */
    public boolean isCopyTemplate() {
        return copyTemplate;
    }
}
