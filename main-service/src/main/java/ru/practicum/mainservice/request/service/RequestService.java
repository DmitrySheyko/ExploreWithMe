package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByUserId(Long userId);

    Request findById(Long requestId);

    List<Request> findAllByEvent(Event event);

    Request save(Request request);

    Event findEventById(Long eventId);
}