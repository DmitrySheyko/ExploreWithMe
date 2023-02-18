package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

import java.util.List;

/**
 * Interface of service class for {@link Request} entity
 *
 * @author DmitrySheyko
 */
public interface RequestService {
    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByUserId(Long userId);
}