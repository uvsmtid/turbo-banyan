package com.spectsys.banyan.service;

import com.spectsys.banyan.entity.StudentEntity;

import java.util.List;

public interface StudentService {

    /**
     * Fetch students by criteria specified as non-null field values
     */
    List<StudentEntity> fetchStudents(
        Long studentId,
        String firstName,
        String lastName,
        String className,
        String nationality
    );

    StudentEntity getStudent(long studentId);

    StudentEntity addNewStudent(StudentEntity studentEntity);

    StudentEntity updateStudent(StudentEntity inputStudentEntity, long studentId);

    void deleteStudent(long studentId);
}
