package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.service.BookService;
import com.example.bookregistryservice.domain.BookResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookDetails(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@RequestBody @Valid BookRequest request) {
        return bookService.create(request);
    }
}
