package com.ruoyi.test.server;

import com.ruoyi.access.tests.server.ModbusTcpServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        classes = ModbusServerTest.TestServerConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE // 禁用 Web 环境
)
@TestPropertySource(properties = {
        "server.port=8112",
        "spring.autoconfigure.exclude=" +
                "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure," + // 排除 Druid 自动配置
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
public class ModbusServerTest {

    @Configuration
    @SpringBootApplication(
            scanBasePackages = "com.ruoyi.access.tests.server", // 仅扫描测试相关包
            exclude = {
                    DataSourceAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class,
                    DataSourceTransactionManagerAutoConfiguration.class
            }
    )
    static class TestServerConfig {
        // 空配置，仅用于启动测试上下文
    }

    private ModbusTcpServer server;

    @BeforeEach
    void setUp() throws Exception {
        server = new ModbusTcpServer(43);
//        server = new ModbusTcpServer(503);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (server != null) {
            server.stop();
            System.out.println("Server stopped successfully.");
        }
    }

    @Test
    public void testModbusTcp() {
        System.out.println("Testing testModbusTcp Server ...");
        try {
            server.start();
            System.out.println("Server started successfully.");
            // 添加一些测试逻辑，例如模拟客户端请求
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}