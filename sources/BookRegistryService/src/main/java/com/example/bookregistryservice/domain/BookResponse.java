package com.example.bookregistryservice.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//Response body for book object
public class BookResponse {

    private int id;
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;
}
