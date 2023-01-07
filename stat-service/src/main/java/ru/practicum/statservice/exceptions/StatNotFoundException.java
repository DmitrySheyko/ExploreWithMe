package ru.practicum.statservice.exceptions;

import lombok.Getter;

@Getter
public class StatNotFoundException extends RuntimeException {

    public StatNotFoundException(String message) {
        super(message);
    }
}