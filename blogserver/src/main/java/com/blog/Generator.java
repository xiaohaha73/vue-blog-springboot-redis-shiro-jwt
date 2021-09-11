package com.blog;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static void main(String[] args) {
        //构建自动生成器对象
        AutoGenerator ag = new AutoGenerator();//这里注意不要到错包，不然调不出方法
        //进行全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/blogserver/src/main/java");
        gc.setAuthor("wang");
        gc.setOpen(false);
        gc.setFileOverride(false);//是否覆盖
        gc.setServiceName("%sService");//去掉service接口的I
        gc.setIdType(IdType.AUTO);//设置自增id的自增方法
        // gc.setSwagger2(true);//设置swagger
        gc.setDateType(DateType.TIME_PACK);
        ag.setGlobalConfig(gc);
        //设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/vueblog?useSSL=true&useUnicode=true&characterEncoding=UTF-8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("73582576");
        dsc.setDbType(DbType.MYSQL); // 设置数据库类型
        ag.setDataSource(dsc);
        //包的配置
        PackageConfig p = new PackageConfig();
        // 包的名称
        p.setModuleName(null);
        // 设置父级包名
        p.setParent("com.blog");
        p.setEntity("pojo");
        p.setMapper("mapper");
        p.setService("service");
        p.setController("controller");
        ag.setPackageInfo(p);
        //策略配置
        StrategyConfig s = new StrategyConfig();
        s.setInclude("m_blog");//设置要生成的表名字
        s.setInclude("m_user");
        s.setNaming((NamingStrategy.underline_to_camel));//驼峰命名法
        s.setColumnNaming((NamingStrategy.underline_to_camel));
        s.setEntityLombokModel(true);
        // 配置逻辑删除的字段
        s.setLogicDeleteFieldName("deleted");
        //自动填充时间配置
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        List<TableFill> list = new ArrayList<>();
        list.add(createTime);
        list.add(updateTime);
        s.setTableFillList(list);
        //乐观锁
//        s.setVersionFieldName("version");
        // restful 风格的控制器
        s.setRestControllerStyle(true);
        s.setControllerMappingHyphenStyle(true);
        ag.setStrategy(s);
//        ag.setTemplateEngine(new FreemarkerTemplateEngine());
        ag.execute();//执行

    }
}
