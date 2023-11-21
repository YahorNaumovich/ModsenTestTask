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

import java.util.*;
import java.util.stream.Collectors;

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

        //Find a book in Registry by Id. If not found throw BookNotFoundException
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        logger.info("getBookDetailById:end -> Id:{} Book:{} - OK", id, book);
        return mapper.toDto(book);
    }

    //TODO:
    // Implement a periodically running job in the BookRegistryService that will compare the list of books
    // in the registry and in the LibraryService, and correct desynchronization that can occur when failures occur
    // during distributed transactions in the BookRegistryService that calls the LibraryService (Done)
    public BookResponse addNewBook(BookRequest request, String token) {
        logger.info("addNewBook:start");

        RestTemplate restTemplate = new RestTemplate();

        //Add new book to Registry
        Book book = bookRepository.save(mapper.fromDto(request));

        //Add new book record to Library
        try {
            HttpHeaders headers = new HttpHeaders();
            String t = token.substring(7);
            headers.setBearerAuth(t);

            HttpEntity<Book> entity = new HttpEntity<>(null, headers);

            String postUrl = LibraryServiceUrl + "libraries/books/" + book.getId();

            logger.info("URL for request to Library Service: {}", postUrl);

            restTemplate.postForObject(postUrl, entity, Book.class);
        } catch (Exception e) {

            //If Library service is not responding - delete recently added book from Registry
            bookRepository.delete(book);
            throw e;
        }

        logger.info("addNewBook:end -> Created Book:{}", book);
        return mapper.toDto(book);
    }

    public BookResponse deleteBookById(int id, String token) {

        logger.info("deleteBookById:start -> Id:{}", id);

        RestTemplate restTemplate = new RestTemplate();

        // Find book in Registry
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        //If found delete it from Library service
        HttpHeaders headers = new HttpHeaders();
        String t = token.substring(7);
        headers.setBearerAuth(t);

        HttpEntity<Book> entity = new HttpEntity<>(null, headers);

        String postUrl = LibraryServiceUrl + "libraries/books/" + book.getId();

        logger.info("URL for request to Library Service: {}", postUrl);

        restTemplate.exchange(postUrl, HttpMethod.DELETE, entity, Book.class);

        //Delete from registry. If something bad happens here, a book is deleted from library but remains in registry
        //There is synchronizeBooksAndRecords method to fix that
        bookRepository.delete(book);

        logger.info("deleteBookById:end -> Id:{} Book:{} - OK", id, book);

        return mapper.toDto(book);
    }

    //TODO: rewrite updateBookDetailById method without using if/else
    public BookResponse updateBookDetailById(BookRequest request, int id) {
        logger.info("updateBookDetailById:start -> Id:{}", id);

        //Find a book to update in Registry
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        logger.info("updateBookDetailById:bookFound -> Book:{}", book);

        //Change required fields
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

        //Save the changes made to the book
        bookRepository.save(book);

        logger.info("updateBookDetailById:end -> Book:{}", book);
        return mapper.toDto(book);
    }

    //TODO: make getBookDetailByIsbn method return list of books since multiple books can have the same ISBN
    public List<BookResponse> getBookDetailByIsbn(String isbn) {
        logger.info("getBookDetailByIsbn:start -> Isbn:{}", isbn);

        //Find all books with matching ISBN
        List<Book> books = bookRepository.findAllByIsbn(isbn);

        logger.info("getBookDetailByIsbn:end -> Isbn:{} - OK", isbn);
        return books.stream().map(mapper::toDto).toList();
    }

    public Page<BookResponse> getAllBooks(Pageable pageable) {
        logger.info("getAllBooks:called");

        //Get all books from Registry (using paging)
        return bookRepository.findAll(pageable).map(mapper::toDto);
    }

    public BookResponse synchronizeBooksAndRecords(String token) {
        logger.info("synchronizeBooksAndRecords:start");

        //Get all book records from Library service
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        String t = token.substring(7);
        headers.setBearerAuth(t);

        HttpEntity<Book> entity = new HttpEntity<>(null, headers);

        String postUrl = LibraryServiceUrl + "libraries";

        logger.info("URL for request to Library Service: {}", postUrl);

        //Get all books from Registry service and Library service
        List<Book> books = bookRepository.findAll();
        List<LinkedHashMap<String,Object>> records = restTemplate.exchange(postUrl, HttpMethod.GET, entity, List.class).getBody();

        //Collect books' and records' Ids to Sets
        Set<Integer> booksIDs = books.stream().map(Book::getId).collect(Collectors.toSet());
        Set<Integer> recordsIDs = new HashSet<>();

        for (LinkedHashMap<String,Object> record : records) recordsIDs.add((Integer) record.get("id"));

        //Get ids of books that weren't deleted from registry due to a failure (if they exist)
        booksIDs.removeAll(recordsIDs);

        //Delete books from Registry
        for (int idForRemoval : booksIDs){
            bookRepository.deleteById(idForRemoval);

            logger.info("synchronizeBooksAndRecords:IdRemoved -> {}", idForRemoval);
        }

        logger.info("synchronizeBooksAndRecords:end");
        return mapper.toDto(books.get(0));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    public static class BookNotFoundException extends NoSuchElementException {
    }
}