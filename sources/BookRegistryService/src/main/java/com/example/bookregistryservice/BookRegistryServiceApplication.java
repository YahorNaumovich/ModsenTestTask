package com.example.bookregistryservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Books API", description = "Books API description"))
@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class BookRegistryServiceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BookRegistryServiceApplication.class, args);
    }

}
