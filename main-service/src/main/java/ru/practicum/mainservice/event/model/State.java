package ru.practicum.mainservice.event.model;

/**
 * Event enumeration class with state of {@link Event}.
 *
 * @author DmitrySheyko
 */
public enum State {
    PENDING,
    PUBLISHED,
    CANCELED
}