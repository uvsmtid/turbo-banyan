package com.spectsys.banyan.service;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentServiceDefault implements StudentService {

    public static final String ERROR_NO_STUDENTS_FOUND = "no students found";
    public static final String ERROR_UNKNOWN_STUDENT_ID = "unknown student id [%s]";

    private final StudentRepository studentRepository;

    @Override
    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public StudentEntity getStudent(final long studentId) {
        return studentRepository
            .findById(studentId)
            .orElseThrow(() -> new NoSuchElementException(format(ERROR_UNKNOWN_STUDENT_ID, studentId)));
    }

    @Override
    public StudentEntity addNewStudent(final StudentEntity studentEntity) {
        return studentRepository.save(studentEntity);
    }

    @Override
    public StudentEntity updateStudent(final StudentEntity inputStudentEntity, final long studentId) {
        return studentRepository
            .findById(studentId)
            .map(existingStudentEntry -> {
                    inputStudentEntity
                        .toBuilder()
                        .studentId(existingStudentEntry.getStudentId())
                        .build();
                    return studentRepository.save(inputStudentEntity);
                }
            )
            .orElseThrow(() -> new NoSuchElementException(format(ERROR_UNKNOWN_STUDENT_ID, studentId)));
    }

    @Override
    public void deleteStudent(long studentId) {
        // deleting by non-existing id throws - let us check first:
        if (studentRepository.findById(studentId).isPresent()) {
            studentRepository.deleteById(studentId);
        }
    }
}
