package ru.practicum.mainservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {
    private List<Error> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private String timestamp;

    public ApiError(List<Error> errors, String message, String reason, HttpStatus status, String timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}