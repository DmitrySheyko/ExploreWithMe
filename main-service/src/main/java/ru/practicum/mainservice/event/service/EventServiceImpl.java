package ru.practicum.mainservice.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryServiceImpl;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventSearchSort;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.service.RequestServiceImpl;
import ru.practicum.mainservice.user.service.UserServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserServiceImpl userServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final RequestServiceImpl requestServiceImpl;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;
    private static final Long TIME_LAG_FOR_CREATE_NEW_EVENT = 2L;
    private static final Long TIME_LAG_FOR_PUBLISH_NEW_EVENT = 2L;

    @Override
    public Event save(Long userId, Event event) {
        userServiceImpl.checkIsObjectInStorage(userId);
        event.setInitiator(userServiceImpl.findById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setState(State.PENDING);
        event.setViews(0);
        return repository.save(event);
    }

    @Override
    public EventFullDto update(AdminUpdateEventDto eventDto) {
        checkIsObjectInStorage(eventDto.getEventId());
        Event event = mapper.toEvent(eventDto);
        event = update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event id={} successfully updated", event.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto publish(Long eventId) {
        checkIsObjectInStorage(eventId);
        Event event = findById(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ValidationException("For publication event should be in status PENDING");
        }
        checkEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event = update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully published", event.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto reject(Long eventId) {
        checkIsObjectInStorage(eventId);
        Event event = findById(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Published event can't be rejected");
        }
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully canceled", event.getId());
        return eventFullDto;
    }

    @Override
    public List<EventFullDto> search(EventAdminSearchDto searchDto, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        EventAdminSearch eventAdminSearch = mapper.toEventAdminSearch(searchDto);
        eventAdminSearch.setCategories(Optional.ofNullable(eventAdminSearch.getCategories())
                .orElse(categoryServiceImpl.findAll().stream()
                        .map(Category::getId)
                        .collect(Collectors.toList())));
        eventAdminSearch.setStates(Optional.ofNullable(eventAdminSearch.getStates())
                .orElse(Arrays.stream(State.values()).collect(Collectors.toList())));
        eventAdminSearch.setRangeStart(Optional.ofNullable(eventAdminSearch.getRangeStart())
                .orElse(LocalDateTime.now()));
        eventAdminSearch.setRangeEnd(Optional.ofNullable(eventAdminSearch.getRangeEnd())
                .orElse(LocalDateTime.MAX));
        Page<Event> eventsPage;
        if (eventAdminSearch.getUsers() == null) {
            eventsPage = repository.searchForAllUsers(eventAdminSearch.getStates(),
                    eventAdminSearch.getCategories(), eventAdminSearch.getRangeStart(), eventAdminSearch.getRangeEnd(),
                    pageable);
        } else {
            eventsPage = repository.searchByUsersSet(eventAdminSearch.getUsers(), eventAdminSearch.getStates(),
                    eventAdminSearch.getCategories(), eventAdminSearch.getRangeStart(), eventAdminSearch.getRangeEnd(),
                    pageable);
        }
        List<EventFullDto> eventDtoList = eventsPage.stream().map(mapper::toFullDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        return eventDtoList;
    }

    @Override
    public EventFullDto add(Long userId, NewEventDto newEventDto) {
        Event event = mapper.toEvent(newEventDto);
        checkEventDateForCreate(event.getEventDate());
        event = save(userId, event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully add", event.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto update(Long userId, PrivateUpdateEventDto eventDto) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsObjectInStorage(eventDto.getEventId());
        Event event = mapper.toEvent(eventDto);
        checkEventDateForCreate(event.getEventDate());
        event = update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully updated", event.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsObjectInStorage(eventId);
        Event event = findById(eventId);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new ValidationException((String.format("User id=%s don't have accesses to information about" +
                    " event id=%s", userId, eventId)));
        }
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Received event id={}", eventId);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Event> eventsPage = repository.findAllByInitiatorId(userId, pageable);
        List<EventShortDto> eventsList = eventsPage.stream().map(mapper::toShortDto).collect(Collectors.toList());
        log.info("Received list of events created by user id={}, page={}, size={}", userId, page, size);
        return eventsList;
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsObjectInStorage(eventId);
        Event event = findById(eventId);
        if (!Objects.equals(State.PENDING, event.getState())) {
            log.warn("Event id={} not canceled because canceling is possible only in PENDING status", eventId);
            throw new ValidationException((String.format("Event id=%s not canceled because canceling is possible " +
                    "only in PENDING status", eventId)));
        }
        event.setState(State.CANCELED);
        event = update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event id={} successfully canceled", eventId);
        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsToEventsOfUser(Long userId, Long eventId) {
        checkIsObjectInStorage(eventId);
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("User id={} don't have access to participation requests of event id={}", userId, event);
            throw new ValidationException(String.format("User id=%s don't have access to participation requests " +
                    "of event id=%s", userId, event));
        }
        List<Request> requestsList = requestServiceImpl.findAllByEvent(event);
        List<ParticipationRequestDto> requestDtoList = requestsList.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of participation requests for event id={} successfully received", eventId);
        return requestDtoList;
    }

    @Override
    public List<EventShortDto> search(EventPublicSearchDto searchDto, int from, int size, EventSearchSort sort) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate"));
        EventPublicSearch eventPublicSearch = mapper.toEventPublicSearch(searchDto);
        eventPublicSearch.setCategories(Optional.ofNullable(eventPublicSearch.getCategories())
                .orElse(categoryServiceImpl.findAll().stream().map(Category::getId).collect(Collectors.toList())));
        eventPublicSearch.setRangeStart(Optional.ofNullable(eventPublicSearch.getRangeStart())
                .orElse(LocalDateTime.now()));
        eventPublicSearch.setRangeEnd(Optional.ofNullable(eventPublicSearch.getRangeEnd())
                .orElse(LocalDateTime.MAX));
        Page<Event> eventsPage;
        if (eventPublicSearch.getOnlyAvailable()) {
            eventsPage = repository.searchAvailable(eventPublicSearch.getText(), eventPublicSearch.getCategories(),
                    eventPublicSearch.getPaid(), eventPublicSearch.getRangeStart(), eventPublicSearch.getRangeEnd(),
                    Status.CONFIRMED.ordinal(), pageable);
        } else {
            eventsPage = repository.searchAll(eventPublicSearch.getText(), eventPublicSearch.getCategories(),
                    eventPublicSearch.getPaid(), eventPublicSearch.getRangeStart(), eventPublicSearch.getRangeEnd(),
                    pageable);
        }
        List<EventShortDto> eventDtoList = eventsPage.stream().map(mapper::toShortDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        if (Objects.equals(sort.name(), "VIEWS")) {
            return eventsPage.stream().map(mapper::toShortDto).sorted().collect(Collectors.toList());
        }
        return eventDtoList;
    }

    @Override
    public EventFullDto getById(Long eventId) {
        checkIsObjectInStorage(eventId);
        Event event = findById(eventId);
        if (event.getPublishedOn() == null) {
            log.info("Not found published event id={}", eventId);
            throw new NotFoundException(String.format("Not found published event id=%s", eventId));
        }
        EventFullDto eventDto = mapper.toFullDto(event);
        log.info("Event id={} successfully received", eventId);
        return eventDto;
    }

    @Override
    public ParticipationRequestDto changeStatusOfParticipationRequest(Long userId, Long eventId, Long requestId,
                                                                      Status status) {
        checkIsObjectInStorage(eventId);
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("User id={} don't have access to participation requests of event id={}", userId, event);
            throw new ValidationException(String.format("User id=%s don't have access to participation requests " +
                    "of event id=%s", userId, event));
        }
        Request request = requestServiceImpl.findById(requestId);
        request.setStatus(status);
        request = requestServiceImpl.save(request);
        ParticipationRequestDto requestDto = requestMapper.toDto(request);
        log.info("Participation requests id={} for event id={} got status {}", requestId, eventId, status.toString());
        return requestDto;
    }

    @Override
    public Event findById(Long eventId) {
        Optional<Event> optionalEvent = repository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)));
    }

    @Override
    public void checkIsObjectInStorage(Long eventId) {
        if (!repository.existsById(eventId)) {
            throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)));
        }
    }

    private Event update(Event event) {
        Event oldEvent = findById(event.getId());
        oldEvent.setAnnotation(Optional.ofNullable(event.getAnnotation()).orElse(oldEvent.getAnnotation()));
        oldEvent.setCategory(Optional.ofNullable(event.getCategory()).orElse(oldEvent.getCategory()));
        oldEvent.setDescription(Optional.ofNullable(event.getDescription()).orElse(oldEvent.getDescription()));
        oldEvent.setEventDate(Optional.ofNullable(event.getEventDate()).orElse(oldEvent.getEventDate()));
        oldEvent.setLocation(Optional.ofNullable(event.getLocation()).orElse(oldEvent.getLocation()));
        oldEvent.setPaid(Optional.ofNullable(event.getPaid()).orElse(oldEvent.getPaid()));
        oldEvent.setParticipantLimit(Optional.ofNullable(event.getParticipantLimit()).orElse(oldEvent.getParticipantLimit()));
        oldEvent.setRequestModeration(Optional.ofNullable(event.getRequestModeration()).orElse(oldEvent.getRequestModeration()));
        oldEvent.setTitle(Optional.ofNullable(event.getTitle()).orElse(oldEvent.getTitle()));
        return repository.save(oldEvent);
    }

    private void checkEventDateForCreate(LocalDateTime eventDate) {
        Duration duration = Duration.between(LocalDateTime.now(), eventDate);
        if (duration.toHours() < TIME_LAG_FOR_CREATE_NEW_EVENT) {
            throw new ValidationException(String.format("Time till the new event should be not less than %s hours",
                    TIME_LAG_FOR_CREATE_NEW_EVENT));
        }
    }

    private void checkEventDateForPublish(LocalDateTime eventDate) {
        Duration duration = Duration.between(LocalDateTime.now(), eventDate);
        if (duration.toHours() < TIME_LAG_FOR_PUBLISH_NEW_EVENT) {
            throw new ValidationException(String.format("Time till the published event should be not less than %s hours",
                    TIME_LAG_FOR_CREATE_NEW_EVENT));
        }
    }
}