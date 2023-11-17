package com.example.bookregistryservice.domain;

import lombok.*;
import org.springframework.http.HttpRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookRequest {

    private int id;
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;

}