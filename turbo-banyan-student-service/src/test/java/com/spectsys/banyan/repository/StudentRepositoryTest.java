package com.spectsys.banyan.repository;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.utils.AbstractIntegratedTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.List;
import java.util.UUID;

import static com.spectsys.banyan.utils.TestData.ALL_STUDENTS;
import static com.spectsys.banyan.utils.TestData.STUDENT_1;
import static com.spectsys.banyan.utils.TestData.STUDENT_1_LAST_NAME;
import static org.junit.Assert.assertEquals;

@Slf4j
@EnableAutoConfiguration
public class StudentRepositoryTest extends AbstractIntegratedTest {

    @Autowired
    private StudentRepository studentRepository;

    @Before
    public void before_test() {

        // clean all:
        for (final StudentEntity studentEntity : studentRepository.findAll()) {
            log.info("delete: {}", studentEntity);
            studentRepository.delete(studentEntity);
        }

        // populate by specific:
        ALL_STUDENTS.forEach(studentEntity -> studentRepository.save(studentEntity));

        studentRepository.flush();
    }

    @Test
    public void find_all_existing_students() {

        // GIVEN

        final long expectedStudentCount = ALL_STUDENTS.size();

        // WHEN

        final List<StudentEntity> allExistingStudents = studentRepository.findAll();

        // THEN

        log.info("allExistingStudents size: {}", allExistingStudents.size());
        assertEquals(expectedStudentCount, allExistingStudents.size());

        for (final StudentEntity existingStudent : allExistingStudents) {
            log.info("existingStudent: {}", existingStudent);
        }
    }

    @Test
    public void find_specific_existing_students() {

        // GIVEN

        final String givenLastName = STUDENT_1_LAST_NAME;
        final StudentEntity givenStudent = STUDENT_1;
        log.info("givenStudent: {}", givenStudent);

        // WHEN

        final List<StudentEntity> existingStudents = studentRepository.findByLastName(givenLastName);

        // THEN

        assertEquals(existingStudents.size(), 1);
        final StudentEntity actualStudent = existingStudents.get(0);
        log.info("actualStudent: {}", actualStudent);

        final StudentEntity expectedStudent = givenStudent
            .toBuilder()
            // borrow id for expected from actual (it is unknown - not verifiable):
            .studentId(actualStudent.getStudentId())
            .build();
        log.info("expectedStudent: {}", expectedStudent);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void test_save_and_find_by_id_again() {

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

        final StudentEntity actualStudentSaved = studentRepository.save(givenStudent);
        log.info("actualStudentSaved: {}", actualStudentSaved);

        // THEN

        final StudentEntity expectedStudent = givenStudent
            .toBuilder()
            // borrow id for expected from actual (as it is generated on save):
            .studentId(actualStudentSaved.getStudentId())
            .build();
        log.info("expectedStudent: {}", actualStudentSaved);

        assertEquals(expectedStudent, actualStudentSaved);

        // WHEN

        final StudentEntity actualStudentFound = studentRepository.findById(expectedStudent.getStudentId()).get();

        // THEN

        assertEquals(expectedStudent, actualStudentFound);
    }
}