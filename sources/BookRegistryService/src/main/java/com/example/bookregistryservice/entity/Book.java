package com.example.bookregistryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.checkerframework.checker.units.qual.Length;

import javax.validation.Valid;

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
    @Column(length = 13)
    @Size(min = 13, max = 13)
    private String isbn;
    private String name;
    private String genre;
    private String description;
    private String author;
}