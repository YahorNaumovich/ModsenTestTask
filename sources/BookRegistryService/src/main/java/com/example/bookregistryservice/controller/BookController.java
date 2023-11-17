package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.entity.User;
import com.example.bookregistryservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
//@SecurityRequirement(name = "Bearer Authentication")
public class BookController {

    private final BookService bookService;
    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book by id")
    public BookResponse getBookDetail(@PathVariable("id") int id) {
        return bookService.getBookDetailById(id);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book")
    public BookResponse addBook(@RequestBody @Valid BookRequest request, @AuthenticationPrincipal User user) {
        return bookService.addNewBook(request, user.getToken());
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
        return bookService.updateBookDetailById(request, id);
    }

    @GetMapping("/books/isbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book by isbn")
    public BookResponse getBookDetailByIsbn(@PathVariable("isbn") String isbn) {
        return bookService.getBookDetailByIsbn(isbn);
    }

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all books")
    public Page<BookResponse> getAllBooks(@PageableDefault() @ParameterObject Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

}