package com.example.bookregistryservice.controller;

import com.example.bookregistryservice.service.BookService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;


@RestControllerAdvice
public class NewExceptionControllerHandler {
    //Handles RestClientException and returns responseEntity with code 503 if Library service is not responding
    @ExceptionHandler(value = {RestClientException.class})
    public ResponseEntity<String> handleRestClientException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Library service is not responding", HttpStatusCode.valueOf(503));
    }

    //Handles BookNotFoundException and returns responseEntity with code 404 if book doesn't exist
    @ExceptionHandler(value = {BookService.BookNotFoundException.class})
    public ResponseEntity<String> handleBookNotFoundException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Book not found", HttpStatusCode.valueOf(404));
    }

    //Handles NullPointerException and returns responseEntity with code 401 if token is null
    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<String> handleNullPointerException(Exception ex) {
        System.out.println(ex.toString());
        return new ResponseEntity<>("Token is null", HttpStatusCode.valueOf(401));
    }
}
