package com.example.bookregistryservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Books API", description = "Books API description"))
public class BookRegistryServiceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BookRegistryServiceApplication.class, args);
    }

}
