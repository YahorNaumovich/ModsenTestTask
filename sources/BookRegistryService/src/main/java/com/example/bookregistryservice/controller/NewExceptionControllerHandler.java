package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.service.BookService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;


@RestControllerAdvice
public class NewExceptionControllerHandler {
    @ExceptionHandler(value = {RestClientException.class})
    public ResponseEntity<String> handleRestClientException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Library service is not responding", HttpStatusCode.valueOf(503));
    }

    @ExceptionHandler(value = {BookService.BookNotFoundException.class})
    public ResponseEntity<String> handleBookNotFoundException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Book not found", HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<String> handleNullPointerException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Token is null", HttpStatusCode.valueOf(401));
    }
}
