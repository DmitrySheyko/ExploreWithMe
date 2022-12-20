package ru.practicum.mainservice.exceptions;

import lombok.Getter;
import lombok.Setter;

public class Error {
    @Getter
    @Setter
    private String error;

    public Error(String error) {
        this.error = error;
    }
}
