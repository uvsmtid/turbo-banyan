package com.spectsys.banyan.config;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.repository.StudentRepository;
import com.spectsys.banyan.rest.EndpointRoot;
import com.spectsys.banyan.rest.EndpointStudents;
import com.spectsys.banyan.service.StudentServiceDefault;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(
    value = {
        EndpointStudents.class,
        EndpointRoot.class,
        StudentServiceDefault.class,
    }
)
@EnableJpaRepositories(
    basePackageClasses = {
        StudentRepository.class,
    }
)
@EntityScan(
    basePackageClasses = {
        StudentEntity.class,
    }
)
public class AppConfig {
}
