package ru.practicum.mainservice.event.service.privateService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.PrivateUpdateEventDto;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventPrivateService {
    private final EventService service;
    private final UserService userService;
    private final RequestService requestService;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;

    public EventFullDto add(Long userId, NewEventDto newEventDto) {
        Event event = mapper.toEvent(newEventDto);
        service.checkEventDateForCreate(event.getEventDate());
        event = service.save(userId, event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully add", event.getId());
        return eventFullDto;
    }

    public EventFullDto update(Long userId, PrivateUpdateEventDto eventDto) {
        userService.checkIsObjectInStorage(userId);
        service.checkIsObjectInStorage(eventDto.getEventId());
        Event event = mapper.toEvent(eventDto);
        service.checkEventDateForCreate(event.getEventDate());
        event = service.update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully updated", event.getId());
        return eventFullDto;
    }

    public EventFullDto getById(Long userId, Long eventId) {
        userService.checkIsObjectInStorage(userId);
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new ValidationException((String.format("User id=%s don't have accesses to full information about event id=%s", userId, eventId)));
        }
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Received event id={}", eventId);
        return eventFullDto;
    }

    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Event> eventsPage = service.findAllByUserId(userId, pageable);
        List<EventShortDto> eventsList = eventsPage.stream().map(mapper::toShortDto).collect(Collectors.toList());
        log.info("Received list of events created by user id={}, page={}, size={}", userId, page, size);
        return eventsList;
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        userService.checkIsObjectInStorage(userId);
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if (!Objects.equals(State.PENDING, event.getState())) {
            log.warn("Event id=%s not canceled because canceling is possible only in PENDING status", eventId);
            throw new ValidationException((String.format("Event id=%s not canceled because canceling is possible only in PENDING status", eventId)));
        }
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event id={} successfully canceled", eventId);
        return eventFullDto;
    }

    public List<ParticipationRequestDto> getRequestsToEventsOfUser(Long userId, Long eventId) {
        service.checkIsObjectInStorage(eventId);
        userService.checkIsObjectInStorage(userId);
        Event event = service.findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("User id={} don't have access to participation requests of event id={}", userId, event);
            throw new ValidationException(String.format("User id=%s don't have access to participation requests of event id=%s", userId, event));
        }
        List<Request> requestsList = requestService.findAllByEvent(event);
        List<ParticipationRequestDto> requestDtoList = requestsList.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of participation requests for event id={} successfully received", eventId);
        return requestDtoList;
    }

    public ParticipationRequestDto changeStatusOfParticipationRequest(Long userId, Long eventId, Long requestId,
                                                                      Status status) {
        service.checkIsObjectInStorage(eventId);
        userService.checkIsObjectInStorage(userId);
        Event event = service.findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("User id={} don't have access to participation requests of event id={}", userId, event);
            throw new ValidationException(String.format("User id=%s don't have access to participation requests of event id=%s", userId, event));
        }
        Request request = requestService.findById(requestId);
        request.setStatus(status);
        request = requestService.save(request);
        ParticipationRequestDto requestDto = requestMapper.toDto(request);
        log.info("Participation requests id={} for event id={} got status {}", requestId, eventId, status.toString());
        return requestDto;
    }
}