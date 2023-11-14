package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book by id")
    public BookResponse getBookDetails(@PathVariable("id") int id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book")
    public BookResponse addBook(@RequestBody @Valid BookRequest request) {
        return bookService.createBook(request);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete book by id")
    public BookResponse deleteBook(@PathVariable("id") int id) {
        return bookService.deleteBookById(id);
    }

    @PatchMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update book details")
    public BookResponse updateBookDetail(@RequestBody @Valid BookRequest request, @PathVariable("id") int id) {
        return bookService.updateBookById(request, id);
    }

    @GetMapping("/books/isbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book by isbn")
    public BookResponse getBookDetailsByIsbn(@PathVariable("isbn") String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @GetMapping("/books/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all books")
    public Page<BookResponse> getAllBooks(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

}
