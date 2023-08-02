package com.springboot_redis;

import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author Aimin
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.springboot_redis.mapper")
@EnableTransactionManagement
public class ApplicationTest {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication application = new SpringApplication(ApplicationTest.class);
        application.run();
    }
}
