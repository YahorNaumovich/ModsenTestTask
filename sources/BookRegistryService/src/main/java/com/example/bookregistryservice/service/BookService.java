package com.example.bookregistryservice.service;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.entity.Book;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import com.example.bookregistryservice.dto.BookDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    private final BookDto mapper;

    public BookResponse getBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return mapper.toDto(book);
    }

    public BookResponse createBook(BookRequest request) {
        Book book = bookRepository.save(mapper.fromDto(request));
        return mapper.toDto(book);
    }

    public BookResponse deleteBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.delete(book);
        return mapper.toDto(book);
    }

    public BookResponse updateBookById(BookRequest request, int id) {
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
        return mapper.toDto(book);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element does not exist")
    static class BookNotFoundException extends NoSuchElementException {}
}
