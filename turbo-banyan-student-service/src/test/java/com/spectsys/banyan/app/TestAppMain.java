package com.spectsys.banyan.app;

import com.spectsys.banyan.config.TestAppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Start service with `local-dev` profile
 */
@SpringBootApplication
@Import(TestAppConfig.class)
@Slf4j
class TestAppMain {

    static {
        System.setProperty("spring.profiles.active", "local-dev");
    }

    public static void main(String[] args) {
        SpringApplication.run(TestAppMain.class, args);
    }
}