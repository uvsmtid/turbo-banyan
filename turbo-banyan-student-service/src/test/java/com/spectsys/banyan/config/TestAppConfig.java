package com.spectsys.banyan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(
    value = {
        AppConfig.class,
    }
)
@Slf4j
public class TestAppConfig {
}
