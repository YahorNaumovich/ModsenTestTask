package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.entity.User;
import com.example.bookregistryservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);
    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void logScheduledMessage(){
        logger.info("This is a scheduled message test");
    }

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
    public BookResponse deleteBook(@PathVariable("id") int id, @AuthenticationPrincipal User user) {
        return bookService.deleteBookById(id, user.getToken());
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
    public List<BookResponse> getBookDetailByIsbn(@PathVariable("isbn") String isbn) {
        return bookService.getBookDetailByIsbn(isbn);
    }

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all books")
    public Page<BookResponse> getAllBooks(@PageableDefault() @ParameterObject Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Synchronize data in BookRegistryService and LibraryService")
    public BookResponse check(@AuthenticationPrincipal User user){
        return bookService.synchronizeBooksAndRecords(user.getToken());
    }

}