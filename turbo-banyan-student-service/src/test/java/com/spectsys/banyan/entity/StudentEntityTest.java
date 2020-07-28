package com.spectsys.banyan.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;

import static com.spectsys.banyan.utils.JsonUtils.fromJson;
import static com.spectsys.banyan.utils.JsonUtils.toPrettyJson;
import static com.spectsys.banyan.utils.ResourceUtils.getFileAsStringFromResource;
import static com.spectsys.banyan.utils.TestData.STUDENT_4;
import static org.junit.Assert.assertEquals;

public class StudentEntityTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String GIVEN_SUDENT_JSON = getFileAsStringFromResource("/sample-student.json");

    @Test
    public void deserialize() {

        // GIVEN

        final String givenStudentJson = GIVEN_SUDENT_JSON;

        // WHEN

        final StudentEntity actualStudent = fromJson(givenStudentJson, StudentEntity.class);

        // THEN

        final StudentEntity expectedStudent = STUDENT_4;

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    @SneakyThrows
    public void serialize() {

        // GIVEN

        final StudentEntity givenStudent = STUDENT_4;
        final String givenStudentJson = GIVEN_SUDENT_JSON;

        // WHEN

        final String actualStudentJson = toPrettyJson(givenStudent);

        // THEN

        assertEquals(OBJECT_MAPPER.readTree(givenStudentJson), OBJECT_MAPPER.readTree(actualStudentJson));
    }
}