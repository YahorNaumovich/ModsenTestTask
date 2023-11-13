package com.example.bookregistryservice.service;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.entity.Book;
import com.example.bookregistryservice.domain.BookResponse;
import com.example.bookregistryservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import com.example.bookregistryservice.dto.BookDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    private final BookDto mapper;

    public BookResponse getBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow();
        return mapper.toDto(book);
    }

    public BookResponse create(BookRequest request) {
        Book book = bookRepository.save(mapper.fromDto(request));
        return mapper.toDto(book);
    }
}
