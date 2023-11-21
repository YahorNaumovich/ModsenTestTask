package com.example.bookregistryservice.repository;

import com.example.bookregistryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book,Integer>{
    List<Book> findAllByIsbn(String isbn);
}
