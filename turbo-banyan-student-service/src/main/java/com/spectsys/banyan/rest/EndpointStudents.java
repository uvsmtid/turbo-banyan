package com.spectsys.banyan.rest;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.spectsys.banyan.rest.MappingConstants.STUDENTS_BASE_PATH;

@RestController
@RequiredArgsConstructor
@Import({
    NotFoundAdvice.class
})
@Slf4j
public class EndpointStudents {

    private final StudentService studentService;

    @GetMapping(STUDENTS_BASE_PATH)
    public List<StudentEntity> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(STUDENTS_BASE_PATH + "{studentId}")
    public StudentEntity getStudent(
        @PathVariable final long studentId
    ) {
        return studentService.getStudent(studentId);
    }

    @PostMapping(STUDENTS_BASE_PATH)
    public StudentEntity addNewStudent(
        @RequestBody final StudentEntity studentEntity
    ) {
        return studentService.addNewStudent(studentEntity);
    }

    @PutMapping(STUDENTS_BASE_PATH + "{studentId}")
    public StudentEntity updateStudent(
        @RequestBody final StudentEntity studentEntity,
        @PathVariable final long studentId
    ) {
        return studentService.updateStudent(studentEntity, studentId);
    }

    @DeleteMapping(STUDENTS_BASE_PATH + "{studentId}")
    void deleteStudent(
        @PathVariable final long studentId
    ) {
        studentService.deleteStudent(studentId);
    }
}
