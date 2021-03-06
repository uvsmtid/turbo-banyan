package com.spectsys.banyan.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.spectsys.banyan.rest.MappingConstants.PARAM_CLASS;
import static com.spectsys.banyan.rest.MappingConstants.PARAM_ID;

@Entity
// yes - all constructors are required:
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@AllArgsConstructor
// ---
@Table(name = "turbo_banyan_student")
@Data
@Builder(toBuilder = true)
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(PARAM_ID)
    private Long studentId;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @JsonProperty(PARAM_CLASS)
    private String className;

    @NonNull
    private String nationality;
}
