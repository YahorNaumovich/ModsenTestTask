package com.example.libraryservice.repository;

import com.example.libraryservice.entity.LibraryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibraryRepository extends JpaRepository<LibraryRecord,Integer> {

    //Find all reserved books in Library
    List<LibraryRecord> findAllByReservedDateIsNotNull();

    //Find all available books in Library
    List<LibraryRecord> findAllByReservedDateIsNull();
}
