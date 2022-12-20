package ru.practicum.mainservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(final NotFoundException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerValidationException(final ValidationException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(HttpStatus.FORBIDDEN.getReasonPhrase())
                .status(HttpStatus.FORBIDDEN.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }




//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handlerExceptions(final Exception e) {
//        return ErrorResponse.builder()
//                .message(e.getMessage())
//                .reason(e.)
//                .status()
//                .timestamp();
//    }
}