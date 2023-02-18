package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventSearchSort;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Interface of service class for {@link Event} entity.
 *
 * @author DmitrySheyko
 */
public interface EventService {

    EventFullDto update(AdminUpdateEventDto eventDto);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);

    List<EventFullDto> adminSearch(EventAdminSearchDto searchDto, int from, int size);

    EventFullDto add(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, PrivateUpdateEventDto eventDto);

    EventFullDto getById(Long userId, Long eventId);

    List<EventShortDto> getAllByUserId(Long userId, int from, int size);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsToEventsOfUser(Long userId, Long eventId);

    List<EventShortDto> publicSearch(EventPublicSearchDto searchDto, int from, int size, EventSearchSort sort);

    EventFullDto getById(Long eventId);

    ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long requestId);
}