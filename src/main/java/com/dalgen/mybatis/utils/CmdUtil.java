package com.dalgen.mybatis.utils;

import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

/**
 * Created by bangis.wangdf on 15/12/3. Desc 控制台输入
 */
public class CmdUtil {

    /**
     * 获取控制台输入
     *
     * @return 控制台命令
     */
    public String consoleInput() {

        Scanner cmdIn = new Scanner(System.in);
        //只有一个DB时
        if (ConfigUtil.getConfig().getDataSourceMap().size() == 1) {
            ConfigUtil.setCmd(chooseTableCmd(cmdIn));
            return ConfigUtil.getCmd();
        }

        System.out.println("==============选择需要从哪个库中生成===============");
        int i = 0;
        for (String dbStr : ConfigUtil.getConfig().getDataSourceMap().keySet()) {
            i++;
            System.out.println(i + " : " + dbStr);
        }
        System.out.println("==============选择需要从哪个库中生成===============");

        int dbInt = cmdIn.nextInt();
        if (dbInt > i && dbInt < 1) {

            System.out.println("输入有误,自动退出[后续改进...]");
            return "q";
        } else {
            i = 0;
            for (String dbStr : ConfigUtil.getConfig().getDataSourceMap().keySet()) {
                i++;
                if (i == dbInt) {
                    ConfigUtil.setCurrentDb(dbStr);
                }
            }
        }

        ConfigUtil.setCmd(chooseTableCmd(cmdIn));
        return ConfigUtil.getCmd();
    }

    private String chooseTableCmd(Scanner cmdIn) {
        System.out.println("输入需要生成的表:");
        System.out.println("===========输入需要生成的表==============");
        System.out.println("-- * 标示所有");
        System.out.println("-- 多表用逗号分隔");
        System.out.println("-- 新表会先生成默认配置,已有表不会影响修改后的SQL");
        System.out.println("-- q 退出");
        System.out.println("===========输入需要生成的表==============");
        String _cmd = cmdIn.next();
        if (StringUtils.isBlank(_cmd)) {
            System.out.println("空输入默认为 * ");
            _cmd = "*";
        }
        return _cmd;
    }

}
