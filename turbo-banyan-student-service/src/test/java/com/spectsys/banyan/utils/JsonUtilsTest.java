package com.spectsys.banyan.utils;

import com.spectsys.banyan.entity.StudentEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.spectsys.banyan.utils.TestData.STUDENT_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class JsonUtilsTest {

    @Test
    public void round_trip_conversion() {

        // GIVEN

        final StudentEntity inputObject = STUDENT_1;

        // WHEN

        final String lineJson = JsonUtils.toLineJson(inputObject);
        final String prettyJson = JsonUtils.toPrettyJson(inputObject);

        final StudentEntity lineOutputObject = JsonUtils.fromJson(lineJson, StudentEntity.class);
        final StudentEntity prettyOutputObject = JsonUtils.fromJson(prettyJson, StudentEntity.class);

        // THEN

        log.info("lineJson: {}", lineJson);
        log.info("prettyJson: {}", prettyJson);

        log.info("lineOutputObject: {}", lineOutputObject);
        log.info("prettyOutputObject: {}", prettyOutputObject);

        assertEquals(inputObject, lineOutputObject);
        assertEquals(inputObject, prettyOutputObject);
    }
}