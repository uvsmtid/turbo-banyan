package com.spectsys.banyan.rest;

import com.spectsys.banyan.utils.AbstractIntegratedTest;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_ROOT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration
public class EndpointRootTest extends AbstractIntegratedTest {

    @Test
    @SneakyThrows
    public void root_request_response() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_ROOT)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andReturn();
    }
}