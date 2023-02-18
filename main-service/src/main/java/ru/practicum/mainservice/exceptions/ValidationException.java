package ru.practicum.mainservice.exceptions;

import lombok.Getter;

/**
 * Class of {@link ValidationException} entity
 *
 * @author DmitrySheyko
 */
@Getter
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}