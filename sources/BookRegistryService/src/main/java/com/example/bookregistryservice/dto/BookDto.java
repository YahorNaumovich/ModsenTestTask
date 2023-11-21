package com.example.bookregistryservice.dto;

import com.example.bookregistryservice.domain.BookRequest;
import com.example.bookregistryservice.entity.Book;
import com.example.bookregistryservice.domain.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookDto {

    //Converts book object to dto
    BookResponse toDto(Book book);

    //Converts dto to book object
    Book fromDto(BookRequest request);
}
