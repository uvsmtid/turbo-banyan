package com.spectsys.banyan.utils;

import com.spectsys.banyan.entity.StudentEntity;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class TestData {

    public final static String STUDENT_1_FIRST_NAME = "Angela";
    public final static String STUDENT_1_LAST_NAME = "Merkel";
    public final static StudentEntity STUDENT_1 = StudentEntity
        .builder()
        .firstName(STUDENT_1_FIRST_NAME)
        .lastName(STUDENT_1_LAST_NAME)
        .build();

    public final static String STUDENT_2_FIRST_NAME = "Boris";
    public final static String STUDENT_2_LAST_NAME = "Johnson";
    public final static StudentEntity STUDENT_2 = StudentEntity
        .builder()
        .firstName(STUDENT_2_FIRST_NAME)
        .lastName(STUDENT_2_LAST_NAME)
        .build();

    public final static List<StudentEntity> ALL_STUDENTS = Collections.unmodifiableList(
        Arrays.asList(
            STUDENT_1,
            STUDENT_2
        )
    );
}
