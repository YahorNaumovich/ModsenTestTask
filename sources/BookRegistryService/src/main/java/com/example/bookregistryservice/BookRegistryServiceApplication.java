package com.example.bookregistryservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "Books API", description = "Books API description"))
@Configuration
@EnableScheduling
//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
@OpenAPIDefinition(info = @Info(title = "Books API", description = "Books API description"),
        security = {@SecurityRequirement(name = "Authorization")})
public class BookRegistryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookRegistryServiceApplication.class, args);
    }

}
