package ru.practicum.statservice.exceptions;

import lombok.Getter;

/**
 * Class of {@link StatNotFoundException} entity
 *
 * @author DmitrySheyko
 */
@Getter
public class StatNotFoundException extends RuntimeException {

    public StatNotFoundException(String message) {
        super(message);
    }
}