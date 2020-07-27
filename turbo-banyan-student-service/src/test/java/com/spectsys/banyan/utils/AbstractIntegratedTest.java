package com.spectsys.banyan.utils;

import com.spectsys.banyan.app.AppMain;
import com.spectsys.banyan.config.TestAppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {
        AppMain.class,
        TestAppConfig.class,
    },
    properties = {
        "spring.profiles.active=local-dev",
    }
)
@AutoConfigureMockMvc
@Slf4j
public abstract class AbstractIntegratedTest {


    @Autowired
    protected MockMvc mockMvc;

    @LocalServerPort
    protected int serverPort;

    @Test
    public void print_url_on_start() {
        log.info("Service started successfully at URL: http://localhost:{}", serverPort);
    }
}
