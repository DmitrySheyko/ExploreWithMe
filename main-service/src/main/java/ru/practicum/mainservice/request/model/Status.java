package ru.practicum.mainservice.request.model;

public enum Status {
    PENDING,
    APPROVED, //TODO вероятно надо убрать тк дублирует конфирмед
    CANCELED,
    REJECTED,
    CONFIRMED
}
