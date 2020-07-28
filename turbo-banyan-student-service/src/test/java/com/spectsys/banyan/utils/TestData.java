package com.spectsys.banyan.utils;

import com.spectsys.banyan.entity.StudentEntity;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@UtilityClass
public class TestData {

    public final static String CLASS_A = "G7";
    public final static String CLASS_B = "88";

    public final static String STUDENT_1_FIRST_NAME = "Angela";
    public final static String STUDENT_1_LAST_NAME = "Merkel";
    public final static StudentEntity STUDENT_1 = StudentEntity
        .builder()
        .firstName(STUDENT_1_FIRST_NAME)
        .lastName(STUDENT_1_LAST_NAME)
        .className(CLASS_A)
        .nationality("Germany")
        .build();

    public final static String STUDENT_2_FIRST_NAME = "Boris";
    public final static String STUDENT_2_LAST_NAME = "Johnson";
    public final static StudentEntity STUDENT_2 = StudentEntity
        .builder()
        .firstName(STUDENT_2_FIRST_NAME)
        .lastName(STUDENT_2_LAST_NAME)
        .className(CLASS_A)
        .nationality("England")
        .build();

    public final static String STUDENT_3_FIRST_NAME = "Nicolas";
    public final static String STUDENT_3_LAST_NAME = "Sarkozy";
    public final static StudentEntity STUDENT_3 = StudentEntity
        .builder()
        .firstName(STUDENT_3_FIRST_NAME)
        .lastName(STUDENT_3_LAST_NAME)
        .className(CLASS_A)
        .nationality("France")
        .build();

    public final static StudentEntity STUDENT_4 = StudentEntity
        .builder()
        .firstName("Muhammadu")
        .lastName("Buhari")
        .className(CLASS_B)
        .nationality("Nigeria")
        .build();

    public static final String STUDENT_5_6_FIRST_NAME = "Alberto";

    public final static StudentEntity STUDENT_5 = StudentEntity
        .builder()
        .studentId(12345L)
        .firstName(STUDENT_5_6_FIRST_NAME)
        .lastName("Fernandez")
        .className(CLASS_B)
        .nationality("Argentina")
        .build();

    public final static StudentEntity STUDENT_6 = StudentEntity
        .builder()
        .firstName(STUDENT_5_6_FIRST_NAME)
        .lastName("Whatever")
        .className(CLASS_B)
        .nationality("Argentina")
        .build();

    public final static List<StudentEntity> ALL_STUDENTS = Collections.unmodifiableList(
        Arrays.asList(
            STUDENT_1,
            STUDENT_2,
            STUDENT_3,
            STUDENT_5,
            STUDENT_6
        )
    );

    // Ensure data is populated so that we have unique students by last name:
    static {
        assertEquals(
            ALL_STUDENTS.size(),
            ALL_STUDENTS
                .stream()
                .map(StudentEntity::getLastName)
                .distinct()
                .count()
        );
    }
}
