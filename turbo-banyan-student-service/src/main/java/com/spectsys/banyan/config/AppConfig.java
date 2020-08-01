package com.spectsys.banyan.config;

import com.spectsys.banyan.entity.StudentEntity;
import com.spectsys.banyan.repository.StudentRepository;
import com.spectsys.banyan.rest.EndpointRoot;
import com.spectsys.banyan.rest.EndpointStudents;
import com.spectsys.banyan.service.StudentServiceDefault;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@EnableSwagger2
public class AppConfig {

    @Bean
    public Docket api() {
        // TODO: configure Swagger UI page
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }
}
