package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.service.BookService;
import com.example.bookregistryservice.domain.BookResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookDetails(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse addBook(@RequestBody @Valid BookRequest request) {
        return bookService.createBook(request);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse deleteBook(@PathVariable("id") int id) {
        return bookService.deleteBookById(id);
    }

    @PatchMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse updateBookDetail(@RequestBody @Valid BookRequest request, @PathVariable("id") int id) {
        return bookService.updateBookById(request, id);
    }

}
