package com.example.bookregistryservice.repository;

import com.example.bookregistryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface BookRepository extends JpaRepository<Book,Integer>{
}
