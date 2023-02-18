package ru.practicum.statservice.exceptions;

import lombok.Getter;

/**
 * Class of {@link StatValidationException} entity
 *
 * @author DmitrySheyko
 */
@Getter
public class StatValidationException extends RuntimeException {

    public StatValidationException(String message) {
        super(message);
    }
}
