package com.example.bookregistryservice.domain;

import lombok.*;

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