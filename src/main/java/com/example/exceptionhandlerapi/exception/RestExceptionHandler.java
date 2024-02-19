package com.example.exceptionhandlerapi.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return handleExceptionInternal(ex, messages(status.value(), ex), headers, status, request);
    }

    private ApiError messages(int status, Exception e) {
        String message = e == null ? "Unknown Error" : e.getMessage();
        String developerMessage = ExceptionUtils.getRootCauseMessage(e);
        return new ApiError(status, message, developerMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex,messages(status.value(),ex),headers,status,request);
    }
    @ExceptionHandler(value = {NotAdminException.class,
    ConstraintViolationException.class,
    IdNotFoundException.class,
    NoMediaTypeException.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex,WebRequest request){
        return handleExceptionInternal(ex,
                messages(HttpStatus.BAD_REQUEST.value(),ex),
                new HttpHeaders(),HttpStatus.BAD_REQUEST,request);
    }
}
