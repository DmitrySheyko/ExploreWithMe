package ru.practicum.mainservice.request.model;

/**
 * Enumeration of statuses for {@link Request} entity
 *
 * @author DmitrySheyko
 */
public enum Status {
    PENDING,
    CANCELED,
    REJECTED,
    CONFIRMED
}