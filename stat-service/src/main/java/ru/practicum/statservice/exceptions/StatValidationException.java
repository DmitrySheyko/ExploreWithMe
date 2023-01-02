package ru.practicum.statservice.exceptions;

import lombok.Getter;

@Getter
public class StatValidationException extends RuntimeException {

    public StatValidationException(String message) {
        super(message);
    }
}
