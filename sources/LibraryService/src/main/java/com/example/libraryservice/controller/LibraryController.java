package com.example.libraryservice.controller;

import com.example.libraryservice.domain.LibraryRecordResponse;
import com.example.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/libraries/reservations")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get reserved books")
    public List<LibraryRecordResponse> getReservedRecords(){
        return libraryService.getReservedRecords();
    }

    @DeleteMapping("/libraries/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a book from the table")
    public LibraryRecordResponse deleteRecord(@PathVariable("id") int id){
        return libraryService.deleteRecord(id);
    }

    @GetMapping("/libraries/books")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get available books")
    public List<LibraryRecordResponse> getAvailableRecords(){
        return libraryService.getAvailableRecords();
    }

    @PutMapping("/libraries/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a book record to indicate it is reserved")
    public LibraryRecordResponse updateReserved(@PathVariable("id") int id){
        return libraryService.updateReserved(id);
    }

    @DeleteMapping("/libraries/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a book record to indicate it is free for reservation")
    public LibraryRecordResponse updateAvailable(@PathVariable("id") int id){
        return libraryService.updateAvailable(id);
    }

    @PostMapping("/libraries/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Adds new available book")
    public LibraryRecordResponse addNewAvailableBook(@PathVariable("id") int id){
        return libraryService.addNewAvailableBook(id);
    }

}
