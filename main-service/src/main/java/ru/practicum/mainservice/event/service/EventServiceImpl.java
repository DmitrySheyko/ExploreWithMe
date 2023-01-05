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
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventSearchSort;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.location.repository.LocationRepository;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private static final Long TIME_LAG_FOR_CREATE_NEW_EVENT = 2L;
    private static final Long TIME_LAG_FOR_PUBLISH_NEW_EVENT = 2L;

    @Override
    @Transactional
    public EventFullDto update(AdminUpdateEventDto eventDto) {
        Event oldEvent = repository.findById(eventDto.getEventId())
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", eventDto.getEventId()))));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", eventDto.getCategory())));
        Event newEvent = EventMapper.toEvent(eventDto, category);
        oldEvent.setAnnotation(Optional.ofNullable(newEvent.getAnnotation()).orElse(oldEvent.getAnnotation()));
        oldEvent.setCategory(Optional.ofNullable(newEvent.getCategory()).orElse(oldEvent.getCategory()));
        oldEvent.setDescription(Optional.ofNullable(newEvent.getDescription()).orElse(oldEvent.getDescription()));
        oldEvent.setEventDate(Optional.ofNullable(newEvent.getEventDate()).orElse(oldEvent.getEventDate()));
        oldEvent.setLocation(Optional.ofNullable(newEvent.getLocation()).orElse(oldEvent.getLocation()));
        oldEvent.setPaid(Optional.ofNullable(newEvent.getPaid()).orElse(oldEvent.getPaid()));
        oldEvent.setParticipantLimit(Optional.ofNullable(newEvent.getParticipantLimit()).orElse(oldEvent.getParticipantLimit()));
        oldEvent.setRequestModeration(Optional.ofNullable(newEvent.getRequestModeration()).orElse(oldEvent.getRequestModeration()));
        oldEvent.setTitle(Optional.ofNullable(newEvent.getTitle()).orElse(oldEvent.getTitle()));
        Event updatedEvent = repository.save(oldEvent);
        EventFullDto eventFullDto = EventMapper.toFullDto(updatedEvent);
        log.info("Event id={} successfully updated", updatedEvent.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", eventId))));
        if (!event.getState().equals(State.PENDING)) {
            throw new ValidationException("For publication event should be in status PENDING");
        }
        checkEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event = repository.save(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event eventId={} successfully published", event.getId());
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", eventId))));
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
                .orElse(categoryRepository.findAll().stream()
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException((String.format("User with id=%s was not found.", userId))));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", newEventDto.getCategory())));
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = EventMapper.toEvent(newEventDto, category, location);
        event.setInitiator(user);
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
        Event oldEvent = repository.findByIdAndInitiatorId(eventDto.getEventId(), userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventDto.getEventId(), userId)));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", eventDto.getCategory())));
        Event newEvent = EventMapper.toEvent(eventDto, category);
        checkEventDateForCreate(oldEvent.getEventDate());
        oldEvent.setAnnotation(Optional.ofNullable(newEvent.getAnnotation()).orElse(oldEvent.getAnnotation()));
        oldEvent.setCategory(Optional.ofNullable(newEvent.getCategory()).orElse(oldEvent.getCategory()));
        oldEvent.setDescription(Optional.ofNullable(newEvent.getDescription()).orElse(oldEvent.getDescription()));
        oldEvent.setEventDate(Optional.ofNullable(newEvent.getEventDate()).orElse(oldEvent.getEventDate()));
        oldEvent.setPaid(Optional.ofNullable(newEvent.getPaid()).orElse(oldEvent.getPaid()));
        oldEvent.setParticipantLimit(Optional.ofNullable(newEvent.getParticipantLimit()).orElse(oldEvent.getParticipantLimit()));
        oldEvent.setTitle(Optional.ofNullable(newEvent.getTitle()).orElse(oldEvent.getTitle()));
        Event updatedEvent = repository.save(oldEvent);
        EventFullDto eventFullDto = EventMapper.toFullDto(updatedEvent);
        log.info("Event eventId={} successfully updated", updatedEvent.getId());
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getById(Long userId, Long eventId) {
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventId, userId)));
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
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventId, userId)));
        if (!Objects.equals(State.PENDING, event.getState())) {
            throw new ValidationException((String.format("Event id=%s not canceled because canceling is possible " +
                    "only in PENDING status", eventId)));
        }
        event.setState(State.CANCELED);
        event = repository.save(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event);
        log.info("Event id={} successfully canceled", eventId);
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsToEventsOfUser(Long userId, Long eventId) {
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventId, userId)));
        List<Request> requestsList = requestRepository.findAllByEvent(event);
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
                .orElse(categoryRepository.findAll().stream().map(Category::getId).collect(Collectors.toSet())));
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
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", eventId))));
        if (event.getPublishedOn() == null) {
            throw new NotFoundException(String.format("Not found published event id=%s", eventId));
        }
        EventFullDto eventDto = EventMapper.toFullDto(event);
        log.info("Event id={} successfully received", eventId);
        return eventDto;
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long requestId) {
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventId, userId)));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request id=%s not found", requestId)));
        if (!Objects.equals(eventId, request.getEvent().getId())) {
            throw new ValidationException(String.format("Request id=%s don't linked with event id=%s", requestId, eventId));
        }
        if (!Objects.equals(request.getStatus(), Status.PENDING)) {
            throw new ValidationException(String.format("Request id=%s has status - %s, for confirmation should be " +
                    "status PENDING", requestId, request.getStatus().name()));
        }
        ParticipationRequestDto requestDto;
        long numberOfConfirmedRequests = event.getRequestsSet().stream()
                .filter(req -> req.getStatus().equals(Status.CONFIRMED))
                .count();
        if (event.getParticipantLimit() == 0 || numberOfConfirmedRequests < event.getParticipantLimit()) {
            request.setStatus(Status.CONFIRMED);
            request = requestRepository.save(request);
            requestDto = RequestMapper.toDto(request);
            log.info("Participation requests id={} for event id={} successfully confirmed", requestId, eventId);
            return requestDto;
        }
        request.setStatus(Status.REJECTED);
        request = requestRepository.save(request);
        requestDto = RequestMapper.toDto(request);
        List<Request> rejectedRequestsList = event.getRequestsSet().stream()
                .filter(req -> req.getStatus().equals(Status.PENDING))
                .peek(req -> req.setStatus(Status.REJECTED))
                .collect(Collectors.toList());
        requestRepository.saveAll(rejectedRequestsList);
        List<Long> rejectedRequestsIdList = rejectedRequestsList.stream()
                .map(Request::getId)
                .collect(Collectors.toList());
        log.info("Request id={} rejected because participants limit is full. Automatically were rejected all " +
                "not confirmed requests, list of id:{}", requestId, rejectedRequestsIdList);
        return requestDto;
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long requestId) {
        repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s with initiator id=%s not found",
                        eventId, userId)));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request id=%s not found", requestId)));
        if (!Objects.equals(eventId, request.getEvent().getId())) {
            throw new ValidationException(String.format("Request id=%s don't linked with event id=%s", requestId, eventId));
        }
        if (!Objects.equals(request.getStatus(), Status.PENDING)) {
            throw new ValidationException(String.format("Request id=%s has status - %s, for rejection should be " +
                    "status PENDING", requestId, request.getStatus().name()));
        }
        request.setStatus(Status.REJECTED);
        request = requestRepository.save(request);
        ParticipationRequestDto requestDto = RequestMapper.toDto(request);
        log.info("Participation requests id={} for event id={} rejected", requestId, eventId);
        return requestDto;
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