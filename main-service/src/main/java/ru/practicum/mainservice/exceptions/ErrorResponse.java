package ru.practicum.mainservice.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private List<Error> errors;
    private String message;
    private String reason;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponse(List<Error> errors, String message, String reason, String status, LocalDateTime timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}