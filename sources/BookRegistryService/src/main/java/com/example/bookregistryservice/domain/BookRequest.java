package com.example.bookregistryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//Request body for book object
public class BookRequest {

    private int id;
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;

}