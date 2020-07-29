package com.spectsys.banyan.rest;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.repository.StudentRepository;
import com.spectsys.banyan.utils.AbstractIntegratedTest;
import com.spectsys.banyan.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_FETCH_STUDENTS;
import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_STUDENTS;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_CLASS;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_FIRST_NAME;
import static com.spectsys.banyan.utils.JsonUtils.toLineJson;
import static com.spectsys.banyan.utils.TestData.ALL_STUDENTS;
import static com.spectsys.banyan.utils.TestData.CLASS_A;
import static com.spectsys.banyan.utils.TestData.CLASS_B;
import static com.spectsys.banyan.utils.TestData.STUDENT_1;
import static com.spectsys.banyan.utils.TestData.STUDENT_1_LAST_NAME;
import static com.spectsys.banyan.utils.TestData.STUDENT_2;
import static com.spectsys.banyan.utils.TestData.STUDENT_2_LAST_NAME;
import static com.spectsys.banyan.utils.TestData.STUDENT_3;
import static com.spectsys.banyan.utils.TestData.STUDENT_3_FIRST_NAME;
import static com.spectsys.banyan.utils.TestData.STUDENT_3_LAST_NAME;
import static com.spectsys.banyan.utils.TestData.STUDENT_5;
import static com.spectsys.banyan.utils.TestData.STUDENT_5_6_FIRST_NAME;
import static com.spectsys.banyan.utils.TestData.STUDENT_6;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integrated test via REST layer
 */
@EnableAutoConfiguration
@AutoConfigureMockMvc
@Slf4j
public class EndpointStudentsTest extends AbstractIntegratedTest {

    @Autowired
    private StudentRepository studentRepository;

    @Before
    public void before_test() {

        cleanRepository();

        // populate:
        ALL_STUDENTS.forEach(studentEntity -> studentRepository.save(studentEntity));

        studentRepository.flush();
    }

    private void cleanRepository() {
        for (final StudentEntity studentEntity : studentRepository.findAll()) {
            log.info("delete: {}", studentEntity);
            studentRepository.delete(studentEntity);
        }
    }

    private StudentEntity getStudentByLastName(final StudentEntity se) {
        final List<StudentEntity> studentEntities = studentRepository.fetchByLastName(se.getLastName());
        // test data is supposed to be populated ensuring single user per last name:
        assertTrue(studentEntities.size() <= 1);
        return studentEntities.get(0);
    }

    @Test
    @SneakyThrows
    public void get_students_from_the_same_class() {

        // GIVEN

        Assert.assertFalse(studentRepository.findAll().isEmpty());

        // WHEN

        final ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_FETCH_STUDENTS)
                    .param(PARAM_CLASS, CLASS_A)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(asList(
                getStudentByLastName(STUDENT_1),
                getStudentByLastName(STUDENT_2),
                getStudentByLastName(STUDENT_3)
            ))))
            .andReturn();
    }


    @Test
    @SneakyThrows
    public void get_students_from_the_same_class_and_first_name() {

        // GIVEN

        Assert.assertFalse(studentRepository.findAll().isEmpty());

        // WHEN

        final ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_FETCH_STUDENTS)
                    .param(PARAM_CLASS, CLASS_B)
                    .param(PARAM_FIRST_NAME, STUDENT_5_6_FIRST_NAME)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(asList(
                getStudentByLastName(STUDENT_5),
                getStudentByLastName(STUDENT_6)
            ))))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void get_all_students_in_non_empty_repository() {

        // GIVEN

        Assert.assertFalse(studentRepository.findAll().isEmpty());

        // WHEN

        final ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_FETCH_STUDENTS)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(
                ALL_STUDENTS.stream().map(this::getStudentByLastName).collect(toList())
            )))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void get_all_students_in_empty_repository() {

        // GIVEN

        cleanRepository();
        Assert.assertTrue(studentRepository.findAll().isEmpty());

        // WHEN

        final ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_FETCH_STUDENTS)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(emptyList())))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void get_specific_user_by_id() {

        // GIVEN

        // Retrieve student and its id via repository to query via REST:
        final List<StudentEntity> byLastName = studentRepository.fetchByLastName(STUDENT_2_LAST_NAME);
        assertEquals(1, byLastName.size());
        final StudentEntity expectedStudent = byLastName.get(0);
        final long studentId = expectedStudent.getStudentId();

        // WHEN

        final ResultActions resultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_STUDENTS + '/' + studentId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(expectedStudent)))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void add_new_user_and_retrieve_it_by_id_to_verify() {

        // GIVEN

        final StudentEntity givenStudent = StudentEntity
            .builder()
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .className(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .build();
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final ResultActions postResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(BASE_PATH_STUDENTS)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(toLineJson(givenStudent))
            );

        // THEN

        final MvcResult postMvcResult = postResultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.firstName", CoreMatchers.is(givenStudent.getFirstName())))
            .andExpect(jsonPath("$.lastName", CoreMatchers.is(givenStudent.getLastName())))
            .andReturn();

        final String actualStudentJson = postMvcResult.getResponse().getContentAsString();
        final StudentEntity actualStudent = JsonUtils.fromJson(actualStudentJson, StudentEntity.class);
        log.info("actualStudent: {}", actualStudent);

        final StudentEntity expectedStudent = givenStudent
            .toBuilder()
            // borrow id for expected from actual (it is unknown - not verifiable):
            .studentId(actualStudent.getStudentId())
            .build();
        log.info("expectedStudent: {}", expectedStudent);

        assertEquals(expectedStudent, actualStudent);

        // WHEN

        final ResultActions getResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_STUDENTS + '/' + expectedStudent.getStudentId())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        getResultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(expectedStudent)))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void update_existing_user_and_retrieve_it_by_id_to_verify() {

        // GIVEN

        final List<StudentEntity> byLastName = studentRepository.fetchByLastName(STUDENT_1_LAST_NAME);
        assertEquals(1, byLastName.size());
        final StudentEntity givenStudent = byLastName.get(0)
            .toBuilder()
            .firstName(STUDENT_3_FIRST_NAME)
            .lastName(STUDENT_3_LAST_NAME)
            .build();
        final long givenStudentId = givenStudent.getStudentId();
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final ResultActions postResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(BASE_PATH_STUDENTS + '/' + givenStudentId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(toLineJson(givenStudent))
            );

        // THEN

        final MvcResult postMvcResult = postResultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.firstName", CoreMatchers.is(givenStudent.getFirstName())))
            .andExpect(jsonPath("$.lastName", CoreMatchers.is(givenStudent.getLastName())))
            .andReturn();

        final String actualStudentJson = postMvcResult.getResponse().getContentAsString();
        final StudentEntity actualStudent = JsonUtils.fromJson(actualStudentJson, StudentEntity.class);
        log.info("actualStudent: {}", actualStudent);

        final StudentEntity expectedStudent = givenStudent
            .toBuilder()
            // borrow id for expected from actual (it is unknown - not verifiable):
            .studentId(actualStudent.getStudentId())
            .build();
        log.info("expectedStudent: {}", expectedStudent);

        assertEquals(expectedStudent, actualStudent);

        // WHEN

        final ResultActions getResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(BASE_PATH_STUDENTS + '/' + expectedStudent.getStudentId())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );

        // THEN

        getResultActions
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andExpect(content().json(toLineJson(expectedStudent)))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void update_non_existing_user_and_fail() {

        // GIVEN

        final long unknownStudentId = 1 + studentRepository
            .findAll()
            .stream()
            .mapToLong(StudentEntity::getStudentId)
            .summaryStatistics()
            .getMax();

        final StudentEntity givenStudent = StudentEntity
            .builder()
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .className(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .build();
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final ResultActions postResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(BASE_PATH_STUDENTS + '/' + unknownStudentId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(toLineJson(givenStudent))
            );

        // THEN

        postResultActions
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void delete_existing_student() {

        // GIVEN

        final List<StudentEntity> byLastName = studentRepository.fetchByLastName(STUDENT_1_LAST_NAME);
        assertEquals(1, byLastName.size());
        final StudentEntity givenStudent = byLastName.get(0)
            .toBuilder()
            .firstName(STUDENT_3_FIRST_NAME)
            .lastName(STUDENT_3_LAST_NAME)
            .build();
        final long givenStudentId = givenStudent.getStudentId();
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final ResultActions postResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete(BASE_PATH_STUDENTS + '/' + givenStudentId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(toLineJson(givenStudent))
            );

        // THEN

        postResultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist())
            .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE))
            .andReturn();
    }

    @Test
    @SneakyThrows
    public void delete_non_existing_student() {

        // GIVEN

        final long unknownStudentId = 1 + studentRepository
            .findAll()
            .stream()
            .mapToLong(StudentEntity::getStudentId)
            .summaryStatistics()
            .getMax();

        final StudentEntity givenStudent = StudentEntity
            .builder()
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .className(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .build();
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final ResultActions postResultActions = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete(BASE_PATH_STUDENTS + '/' + unknownStudentId)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .content(toLineJson(givenStudent))
            );

        // THEN

        postResultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist())
            .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE))
            .andReturn();
    }
}