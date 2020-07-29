package com.spectsys.banyan.rest;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_ROOT;

/**
 * Basic response with instance id and request counter
 */
@RestController
@Slf4j
public class EndpointRoot {

    private final static String INSTANCE_ID = UUID.randomUUID().toString();
    private final static AtomicLong REQUEST_COUNTER = new AtomicLong(0);

    @Data
    @Builder
    private static class RootResponse {

        private final Instant timestamp;
        private final String instanceId;
        private final long requestNumber;
    }

    @RequestMapping(BASE_PATH_ROOT)
    // TODO: remove before prod release
    public RootResponse root() {
        final RootResponse rootResponse = RootResponse
            .builder()
            .instanceId(INSTANCE_ID)
            .timestamp(Instant.now())
            .requestNumber(REQUEST_COUNTER.incrementAndGet())
            .build();
        log.info("rootResponse: {}", rootResponse);
        return rootResponse;
    }
}
