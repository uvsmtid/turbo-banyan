package com.spectsys.banyan.repository;

import com.spectsys.banyan.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    default List<StudentEntity> fetchByLastName(String lastName) {
        return fetchByAny(
            null,
            null,
            lastName,
            null,
            null
        );
    }

    @Query(
        "select s from StudentEntity s " +
            // all params are optional
            "where (:studentId is null or s.studentId = :studentId) " +
            "and (:firstName is null or s.firstName = :firstName) " +
            "and (:lastName is null or s.lastName = :lastName) " +
            "and (:className is null or s.className = :className) " +
            "and (:nationality is null or s.nationality = :nationality) "
    )
    List<StudentEntity> fetchByAny(
        @Param("studentId") Long studentId,
        @Param("firstName") String firstName,
        @Param("lastName") String lastName,
        @Param("className") String className,
        @Param("nationality") String nationality
    );
}
