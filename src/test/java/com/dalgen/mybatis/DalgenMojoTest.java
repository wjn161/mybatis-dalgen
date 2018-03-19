package com.dalgen.mybatis;

import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

import com.dalgen.mybatis.utils.CmdUtil;
import com.dalgen.mybatis.utils.ConfigUtil;

/**
 * Created by bangis.wangdf on 15/12/5.
 * Desc
 */
public class DalgenMojoTest extends BaseTest {

    public void testExecute() throws Exception {
        MavenProject project = new MavenProject();
        project.setName("");
        DalgenMojo dalgenMojo = new DalgenMojo(outputDirectory,templateDirectory,config,project,true);
        CmdUtil cmdUtil = Mockito.mock(CmdUtil.class);
        Mockito.when(cmdUtil.consoleInput()).thenReturn(ConfigUtil.getCmd());
        dalgenMojo.setCmdUtil(cmdUtil);

        dalgenMojo.execute();
    }
}