package ru.practicum.statservice.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class of {@link ErrorResponse} entity.
 * Required for sending information about exceptions in {@link ErrorHandler}
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
public class ErrorResponse {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponse(List<String> errors, String message, String reason, String status, LocalDateTime timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}