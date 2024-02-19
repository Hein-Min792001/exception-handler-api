package com.example.exceptionhandlerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class NoMediaTypeException extends ResponseStatusException {
    public NoMediaTypeException(){
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"Not Supported Media Type!");
    }
}
