package com.reportcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author Sendy
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.reportcenter.project.*.*.dao")
public class ReportCenterApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run( ReportCenterApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统启动成功   ლ(´ڡ`ლ)ﾞ \n" +
                " .-------.       .-------.         \n" +
                " |  _ _   \\     |  _ _   \\       \n" +
                " | ( ' )  |      | ( ' )  |        \n" +
                " |(_ o _) /      |(_ o _) /        \n" +
                " | (_,_).'       | (_,_).'         \n" +
                " |  |\\ \\       |  |\\ \\         \n" +
                " |  | \\ `'   /  |  | \\ `'   /    \n" +
                " |  |  \\    /   |  |  \\    /     \n" +
                " ''-'   `'-'     ''-'   `'-'         ");
    }
}