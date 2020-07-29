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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_FETCH_STUDENTS;
import static com.spectsys.banyan.rest.MappingConstants.BASE_PATH_STUDENTS;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_CLASS;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_FIRST_NAME;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_ID;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_LAST_NAME;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_NATIONALITY;

@RestController
@RequiredArgsConstructor
@Import({
    NotFoundAdvice.class
})
@Slf4j
public class EndpointStudents {

    private final StudentService studentService;

    ///////////////////////////////////////////////////////////////////////////
    // Read queries

    @GetMapping(BASE_PATH_STUDENTS + "/{studentId}")
    public StudentEntity getStudent(
        @PathVariable
        final long studentId
    ) {
        return studentService.getStudent(studentId);
    }

    @GetMapping(BASE_PATH_FETCH_STUDENTS)
    public List<StudentEntity> fetchStudents(
        @RequestParam(name = PARAM_ID, required = false) Long studentId,
        @RequestParam(name = PARAM_FIRST_NAME, required = false) String firstName,
        @RequestParam(name = PARAM_LAST_NAME, required = false) String lastName,
        @RequestParam(name = PARAM_CLASS, required = false) String className,
        @RequestParam(name = PARAM_NATIONALITY, required = false) String nationality
    ) {
        return studentService.fetchStudents(
            studentId,
            firstName,
            lastName,
            className,
            nationality
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Write operations

    @PostMapping(BASE_PATH_STUDENTS)
    public StudentEntity addNewStudent(
        @RequestBody
        final StudentEntity studentEntity
    ) {
        return studentService.addNewStudent(studentEntity);
    }

    @PutMapping(BASE_PATH_STUDENTS + "/{studentId}")
    public StudentEntity updateStudent(
        @RequestBody
        final StudentEntity studentEntity,
        @PathVariable
        final long studentId
    ) {
        return studentService.updateStudent(studentEntity, studentId);
    }

    @DeleteMapping(BASE_PATH_STUDENTS + "/{studentId}")
    void deleteStudent(
        @PathVariable
        final long studentId
    ) {
        studentService.deleteStudent(studentId);
    }
}
