package com.example.bookregistryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder=true)
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;
}