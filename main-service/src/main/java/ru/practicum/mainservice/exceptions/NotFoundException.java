package ru.practicum.mainservice.exceptions;

import lombok.Getter;

/**
 * Class of {@link NotFoundException} entity
 *
 * @author DmitrySheyko
 */
@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}