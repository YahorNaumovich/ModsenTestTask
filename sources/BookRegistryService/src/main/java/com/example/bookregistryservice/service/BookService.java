package com.example.bookregistryservice.service;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.dto.BookDto;
import com.example.bookregistryservice.entity.Book;
import com.example.bookregistryservice.entity.User;
import com.example.bookregistryservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookDto mapper;

    Logger logger = LoggerFactory.getLogger(BookService.class);

    @Value("${LIBRARY_URL}")
    String LibraryServiceUrl;

    public BookResponse getBookDetailById(int id) {
        logger.info("getBookDetailById:start -> Id:{}", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        logger.info("getBookDetailById:end -> Id:{} Book:{} - OK", id, book);
        return mapper.toDto(book);
    }

    public BookResponse addNewBook(BookRequest request, String token) {
        logger.info("addNewBook:start");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RestTemplate restTemplate = new RestTemplate();
        Book book = bookRepository.save(mapper.fromDto(request));
        try {
            HttpHeaders headers = new HttpHeaders();
            String t = token.substring(7);
            headers.setBearerAuth(t);
            HttpEntity<Book> entity = new HttpEntity<>(null, headers);
            String postUrl = LibraryServiceUrl + "libraries/books/" + book.getId();
            logger.info("URL for request to Library Service: {}", postUrl);
            restTemplate.postForObject(postUrl, entity, Book.class);
        } catch (RestClientException | NullPointerException e) {
            bookRepository.delete(book);
            throw e;
        }
        logger.info("addNewBook:end -> Created Book:{}", book);
        return mapper.toDto(book);
    }

    public BookResponse deleteBookById(int id, String token) {
        logger.info("deleteBookById:start -> Id:{}", id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RestTemplate restTemplate = new RestTemplate();
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        HttpHeaders headers = new HttpHeaders();
        String t = token.substring(7);
        headers.setBearerAuth(t);
        HttpEntity<Book> entity = new HttpEntity<>(null, headers);
        String postUrl = LibraryServiceUrl + "libraries/books/" + book.getId();
        logger.info("URL for request to Library Service: {}", postUrl);
        restTemplate.exchange(postUrl, HttpMethod.DELETE, entity,Book.class);
        bookRepository.delete(book);
        logger.info("deleteBookById:end -> Id:{} Book:{} - OK", id, book);
        return mapper.toDto(book);
    }

    public BookResponse updateBookDetailById(BookRequest request, int id) {
        logger.info("updateBookDetailById:start -> Id:{}", id);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        logger.info("updateBookDetailById:bookFound -> Book:{}", book);
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
        logger.info("updateBookDetailById:end -> Book:{}", book);
        return mapper.toDto(book);
    }

    public BookResponse getBookDetailByIsbn(String isbn) {
        logger.info("getBookDetailByIsbn:start -> Isbn:{}", isbn);
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);
        logger.info("getBookDetailByIsbn:end -> Isbn:{} - OK", isbn);
        return mapper.toDto(book);
    }

    public Page<BookResponse> getAllBooks(Pageable pageable) {
        logger.info("getAllBooks:called");
        return bookRepository.findAll(pageable).map(mapper::toDto);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    public static class BookNotFoundException extends NoSuchElementException {
    }
}