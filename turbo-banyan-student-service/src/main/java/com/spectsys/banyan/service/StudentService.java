package com.spectsys.banyan.service;

import com.spectsys.banyan.entity.StudentEntity;

import java.util.List;

public interface StudentService {

    List<StudentEntity> getAllStudents();

    StudentEntity getStudent(long studentId);

    StudentEntity addNewStudent(StudentEntity studentEntity);

    StudentEntity updateStudent(StudentEntity inputStudentEntity, long studentId);

    void deleteStudent(long studentId);
}
