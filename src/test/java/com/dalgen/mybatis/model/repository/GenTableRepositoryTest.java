package com.dalgen.mybatis.model.repository;

import com.dalgen.mybatis.model.config.CfTable;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by bangis.wangdf on 15/12/10.
 * Desc
 */
public class GenTableRepositoryTest extends TestCase {

    public void testGainGenTable() throws Exception {
        String file ="/work/workspace/mvnPlugin/mybatis-dalgen/src/test/resources/dalgen/fporgassetcenterTables/FP_OAC_ORG_TXN_TASK.xml";

        CfTableRepository gtr = new CfTableRepository();
        CfTable genTable = gtr.gainCfTable(new File(file));
    }


}