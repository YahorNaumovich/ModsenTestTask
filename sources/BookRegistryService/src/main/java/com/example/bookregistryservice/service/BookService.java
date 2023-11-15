package com.example.bookregistryservice.service;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.dto.BookDto;
import com.example.bookregistryservice.entity.Book;
import com.example.bookregistryservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService{

    private final BookRepository bookRepository;

    private final BookDto mapper;

    public BookResponse getBookDetailById(int id) {
        System.out.printf("getBookDetailById:start -> Id:{%d}\n", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        System.out.printf("getBookDetailById:end -> Id:{%d} Book:{%s} - OK\n", id, book.toString());
        return mapper.toDto(book);
    }

    public BookResponse addNewBook(BookRequest request) {
        System.out.print("addNewBook:start\n");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = null;
        try {
            headers = restTemplate.headForHeaders("http://localhost:8081//libraries/books");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
        Book book = bookRepository.save(mapper.fromDto(request));
        restTemplate.postForObject("http://localhost:8081/libraries/books/" + book.getId(), null, Book.class);
        System.out.printf("addNewBook:end -> Created Book:{%s}\n", book);
        return mapper.toDto(book);
    }

    public BookResponse deleteBookById(int id) {
        System.out.printf("deleteBookById:start -> Id:{%d}\n", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.delete(book);
        System.out.printf("deleteBookById:end -> Id:{%d} Book:{%s} - OK\n", id, book);
        return mapper.toDto(book);
    }

    public BookResponse updateBookDetailById(BookRequest request, int id) {
        System.out.printf("updateBookDetailById:start -> Id:{%d}\n", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        if (request.getName() != null)
            book.setName(request.getName());
        if (request.getIsbn() != null)
            book.setIsbn(request.getIsbn());
        if (request.getAuthor() != null)
            book.setAuthor(request.getAuthor());
        if (request.getGenre() != null)
            book.setGenre(request.getGenre());
        if (request.getDescription() != null)
            book.setDescription(request.getDescription());
        bookRepository.save(book);
        System.out.printf("updateBookDetailById:end -> Id:{%d} - OK\n", id);
        return mapper.toDto(book);
    }

    public BookResponse getBookDetailByIsbn(String isbn){
        System.out.printf("getBookDetailByIsbn:start -> Isbn:{%s}\n", isbn);
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);
        System.out.printf("getBookDetailByIsbn:end -> Isbn:{%s} - OK\n", isbn);
        return mapper.toDto(book);
    }

    public Page<BookResponse> getAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable).map(mapper::toDto);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    static class BookNotFoundException extends NoSuchElementException {}
}