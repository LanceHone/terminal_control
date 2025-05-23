package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAsync
@EnableScheduling
public class SecurityApplication
{
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        SpringApplication.run(SecurityApplication.class, args);
        System.out.println("\uD83D\uDD30\uD83D\uDD30\uD83D\uDD30ﾞ  " +
                "⛨安全项目启动成功⛨   ლ(´ڡ`ლ) \uD83D\uDEE1⚔\uD83D\uDEE1ﾞ  \n");
    }
}
