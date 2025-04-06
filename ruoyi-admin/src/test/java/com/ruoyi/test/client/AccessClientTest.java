package com.ruoyi.test.client; // 独立测试包

import com.ruoyi.access.tests.client.AccessTcpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

@SpringBootTest(
        classes = AccessClientTest.TestClientConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "spring.autoconfigure.exclude=" +
                "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration"
})
public class AccessClientTest {

    @Configuration
    @SpringBootApplication(
            scanBasePackages = "com.ruoyi.access.tests.client", // 明确扫描范围
            exclude = {
                    DataSourceAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class,
                    DataSourceTransactionManagerAutoConfiguration.class,
                    QuartzAutoConfiguration.class,
                    SecurityAutoConfiguration.class
            }
    )
    static class TestClientConfig {
    }

    @Test
    public void testYourAccess() {
        System.out.println("Testing testYourAccess Client ...");
        AccessTcpClient client = new AccessTcpClient("127.0.0.1", 43);
        try {
            client.connect();
            byte[] request = {/* 请求数据 */
                    0x01,  // Transaction Identifier
                    0x00,  // Protocol Identifier
                    0x00, 0x01,  // Length
                    0x03,  // Unit Identifier (Function Code 0x03: Read Holding Registers)
                    0x00, 0x00,  // Starting Address
                    0x00, 0x01   // Quantity of Registers
            };
            client.sendRequest(request);
            byte[] response = client.receiveResponse();
            /* 处理响应帧 */
            System.out.println("Response: " + Arrays.toString(response));
            System.out.println("Testing testYour Client Done.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}