package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.location.service.LocationServiceImpl;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.service.RequestServiceImpl;
import ru.practicum.mainservice.user.service.UserServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserServiceImpl userServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;
    private final RequestServiceImpl requestServiceImpl;
    private final LocationServiceImpl locationServiceImpl;
    private static final Long TIME_LAG_FOR_CREATE_NEW_EVENT = 2L;
    private static final Long TIME_LAG_FOR_PUBLISH_NEW_EVENT = 2L;

    @Override
    @Transactional
    public EventFullDto update(AdminUpdateEventDto eventDto) {
        Category category = categoryServiceImpl.findById(eventDto.getCategory());
        Event event = EventMapper.toEvent(eventDto, category);
        event = update(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event id={} successfully updated", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = findById(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ValidationException("For publication event should be in status PENDING");
        }
        checkEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event = update(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event eventId={} successfully published", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = findById(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Published event can't be rejected");
        }
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event eventId={} successfully canceled", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> adminSearch(EventAdminSearchDto searchDto, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        EventAdminSearch eventAdminSearch = EventMapper.toEventAdminSearch(searchDto);
        eventAdminSearch.setCategories(Optional.ofNullable(eventAdminSearch.getCategories())
                .orElse(categoryServiceImpl.findAll().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())));
        eventAdminSearch.setStates(Optional.ofNullable(eventAdminSearch.getStates())
                .orElse(Arrays.stream(State.values()).collect(Collectors.toSet())));
        eventAdminSearch.setRangeStart(Optional.ofNullable(eventAdminSearch.getRangeStart())
                .orElse(LocalDateTime.now()));
        eventAdminSearch.setRangeEnd(Optional.ofNullable(eventAdminSearch.getRangeEnd())
                .orElse(LocalDateTime.now().plusYears(100)));
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
        List<EventFullDto> eventDtoList = eventsPage.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        return eventDtoList;
    }

    @Override
    @Transactional
    public EventFullDto add(Long userId, NewEventDto newEventDto) {
        Category category = categoryServiceImpl.findById(newEventDto.getCategory());
        Location location = locationServiceImpl.save(newEventDto.getLocation());
        Event event = EventMapper.toEvent(newEventDto, category, location);
        event.setInitiator(userServiceImpl.findById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setState(State.PENDING);
        event.setViews(0);
        checkEventDateForCreate(event.getEventDate());
        repository.save(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event eventId={} successfully add", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, PrivateUpdateEventDto eventDto) {
        userServiceImpl.checkIsObjectInStorage(userId);
        Category category = categoryServiceImpl.findById(eventDto.getCategory());
        Event event = EventMapper.toEvent(eventDto, category);
        if (!(Objects.equals(userId, findById(eventDto.getEventId()).getInitiator().getId()))) {
            throw new ValidationException((String.format("User id=%s don't have accesses to update " +
                    " event id=%s", userId, event.getId())));
        }
        checkEventDateForCreate(event.getEventDate());
        event = update(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event eventId={} successfully updated", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getById(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findById(eventId);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new ValidationException((String.format("User id=%s don't have accesses to information about" +
                    " event id=%s", userId, eventId)));
        }
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Received event id={}", eventId);
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Event> eventsPage = repository.findAllByInitiatorId(userId, pageable);
        List<EventShortDto> eventsList = eventsPage.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
        log.info("Received list of events created by user id={}, page={}, size={}", userId, page, size);
        return eventsList;
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findById(eventId);
        if (!Objects.equals(State.PENDING, event.getState())) {
            log.warn("Event id={} not canceled because canceling is possible only in PENDING status", eventId);
            throw new ValidationException((String.format("Event id=%s not canceled because canceling is possible " +
                    "only in PENDING status", eventId)));
        }
        if (!(Objects.equals(userId, event.getInitiator().getId()))) {
            throw new ValidationException((String.format("User id=%s don't have accesses to cancel" +
                    " event id=%s", userId, eventId)));
        }
        event.setState(State.CANCELED);
        event = update(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event id={} successfully canceled", eventId);
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsToEventsOfUser(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.warn("User id={} don't have access to participation requests of event id={}", userId, event);
            throw new ValidationException(String.format("User id=%s don't have access to participation requests " +
                    "of event id=%s", userId, event));
        }
        List<Request> requestsList = requestServiceImpl.findAllByEvent(event);
        List<ParticipationRequestDto> requestDtoList = requestsList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of participation requests for event id={} successfully received", eventId);
        return requestDtoList;
    }

    @Override
    @Transactional
    public List<EventShortDto> publicSearch(EventPublicSearchDto searchDto, int from, int size, EventSearchSort sort) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate"));
        EventPublicSearch eventPublicSearch = EventMapper.toEventPublicSearch(searchDto);
        eventPublicSearch.setCategories(Optional.ofNullable(eventPublicSearch.getCategories())
                .orElse(categoryServiceImpl.findAll().stream().map(Category::getId).collect(Collectors.toSet())));
        eventPublicSearch.setRangeStart(Optional.ofNullable(eventPublicSearch.getRangeStart())
                .orElse(LocalDateTime.now()));
        eventPublicSearch.setRangeEnd(Optional.ofNullable(eventPublicSearch.getRangeEnd())
                .orElse(LocalDateTime.now().minusYears(100)));
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
        List<EventShortDto> eventDtoList = eventsPage.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        if (Objects.equals(sort.name().toUpperCase(), EventSearchSort.VIEWS.name().toUpperCase())) {
            return eventsPage.stream().map(EventMapper::toShortDto).sorted().collect(Collectors.toList());
        }
        return eventDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getById(Long eventId) {
        Event event = findById(eventId);
        if (event.getPublishedOn() == null) {
            log.info("Not found published event id={}", eventId);
            throw new NotFoundException(String.format("Not found published event id=%s", eventId));
        }
        EventFullDto eventDto = EventMapper.toFullDto(event);
        log.info("Event id={} successfully received", eventId);
        return eventDto;
    }

    @Override
    @Transactional
    public ParticipationRequestDto changeStatusOfParticipationRequest(Long userId, Long eventId, Long requestId,
                                                                      Status status) {
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
        ParticipationRequestDto requestDto = RequestMapper.toDto(request);
        log.info("Participation requests id={} for event id={} got status {}", requestId, eventId, status.toString());
        return requestDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(Long eventId) {
        Optional<Event> optionalEvent = repository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)));
    }

//    @Override  //TODO возможно убрать
//    @Transactional(readOnly = true)
//    public void checkIsObjectInStorage(Set<Long> eventSet) {
//        for (Long eventId : eventSet) {
//            if (!repository.existsById(eventId)) {
//                log.warn("Event id={} was not found", eventId);
//                throw new NotFoundException((String.format("Event id=%s was not found.", eventId)));
//            }
//        }
//    }

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

    public Set<Event> findAllById(Set<Long> idList){
       return new HashSet<>(repository.findAllById(idList));
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