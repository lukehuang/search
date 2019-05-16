package com.common.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class CodeGenerator {

    public static void main(String[] args) {
        /* 代码生成器 */
        AutoGenerator mpg = new AutoGenerator();

        /* 全局配置 */
        GlobalConfig gc = new GlobalConfig();
        final String projectPath = System.getProperty("user.dir");
        final String projectName = "/ssm_es"; /* Idea多Module项目，getProperty只能获取到根目录 */
        gc.setOutputDir(projectPath + projectName + "/src/main/java");
        gc.setAuthor("generator");
        gc.setOpen(false);
        gc.setActiveRecord(true);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        /* 数据源配置 */
        InputStream inputStream = CodeGenerator.class.getResourceAsStream("/config/application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(properties.getProperty("jdbc.url"));
        dsc.setDriverName(properties.getProperty("jdbc.driverClassName"));
        dsc.setUsername(properties.getProperty("jdbc.username"));
        dsc.setPassword(properties.getProperty("jdbc.password"));
        mpg.setDataSource(dsc);

        /* 包配置 */
        final PackageConfig pc = new PackageConfig();
        final String moduleName = scanner("模块名"); /* 输入与Application平行的包名，如demo */
        pc.setModuleName(moduleName);
        pc.setParent("com");
        pc.setEntity("model");
        pc.setMapper("mapper");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        /* 自定义配置 */
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/generator/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                /* 自定义输入文件名称 */
                return projectPath + projectName + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + ".xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        /* 策略配置 */
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /* 读取控制台内容 */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
