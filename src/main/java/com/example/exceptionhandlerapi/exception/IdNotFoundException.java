package com.example.exceptionhandlerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IdNotFoundException extends ResponseStatusException {
    public IdNotFoundException(int id){
        super(HttpStatus.NOT_FOUND,id+ "not found!");
    }
}
