package com.example.exceptionhandlerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAdminException extends ResponseStatusException {
    public NotAdminException(){
        super(HttpStatus.BAD_REQUEST,"Name cannot be admin");
    }
}
