package ru.practicum.mainservice.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.dto.EventAdminSearch;
import ru.practicum.mainservice.event.dto.EventPublicSearch;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.user.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    EventRepository repository;
    UserService userService;
    private static final Long TIME_LAG_FOR_CREATE_NEW_EVENT = 2L;
    private static final Long TIME_LAG_FOR_PUBLISH_NEW_EVENT = 2L;

    public Event save(Long userId, Event event) {
        userService.checkIsObjectInStorage(userId);
        event.setInitiator(userService.findById(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setState(State.PENDING);
//        event.setViews(0);
        return repository.save(event);
    }

    public Event update(Event event) {
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

    public Page<Event> searchAvailable(EventPublicSearch eventPublicSearch, Pageable pageable) {
        return repository.searchAvailable(eventPublicSearch.getText(),
                eventPublicSearch.getCategories(),
                eventPublicSearch.getPaid(),
                eventPublicSearch.getRangeStart(),
                eventPublicSearch.getRangeEnd(),
                Status.APPROVED.ordinal(),
                pageable);
    }

    public Page<Event> searchAll(EventPublicSearch eventPublicSearch, Pageable pageable) {
        return repository.searchAll(eventPublicSearch.getText(),
                eventPublicSearch.getCategories(),
                eventPublicSearch.getPaid(),
                eventPublicSearch.getRangeStart(),
                eventPublicSearch.getRangeEnd(),
                pageable);
    }

    public Page<Event> searchByUsersSet(EventAdminSearch eventAdminSearch, Pageable pageable) {
        return repository.searchByUsersSet(eventAdminSearch.getUsers(), eventAdminSearch.getStates(),
                eventAdminSearch.getCategories(), eventAdminSearch.getRangeStart(), eventAdminSearch.getRangeEnd(),
                pageable);
    }

    public Page<Event> searchForAllUsers(EventAdminSearch eventAdminSearch, Pageable pageable) {
        return repository.searchForAllUsers(eventAdminSearch.getStates(),
                eventAdminSearch.getCategories(), eventAdminSearch.getRangeStart(), eventAdminSearch.getRangeEnd(),
                pageable);
    }

    public Event findById(Long eventId) {
        Optional<Event> optionalEvent = repository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)),
                "The required object was not found.");
    }

    public Page<Event> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public List<Event> findAll() {
        return repository.findAll();
    }

    public Page<Event> findAllByUserId(Long userId, Pageable pageable) {
        return repository.findAllByInitiatorId(userId, pageable);
    }

    public Map<String, Long> deleteById(Long eventId) {
        repository.deleteById(eventId);
        return Map.of("Successfully deleted event id=", eventId);
    }

    public void checkIsObjectInStorage(Event event) {
        if (!repository.existsById(event.getId())) {
            throw new NotFoundException((String.format("Event with id=%s was not found.", event.getId())),
                    "The required object was not found.");
        }
    }

    public void checkIsObjectInStorage(Long eventId) {
        if (!repository.existsById(eventId)) {
            throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)),
                    "The required object was not found.");
        }
    }

    public void checkEventDateForCreate(LocalDateTime eventDate) {
        Duration duration = Duration.between(LocalDateTime.now(), eventDate);
        if (duration.toHours() < TIME_LAG_FOR_CREATE_NEW_EVENT) {
            throw new ValidationException(String.format("Time till the event should be not less than %s hours",
                    TIME_LAG_FOR_CREATE_NEW_EVENT), "For the requested operation the conditions are not met");
        }
    }

    public void checkEventDateForPublish(LocalDateTime eventDate) {
        Duration duration = Duration.between(LocalDateTime.now(), eventDate);
        if (duration.toHours() < TIME_LAG_FOR_PUBLISH_NEW_EVENT) {
            throw new ValidationException(String.format("Time till the event should be not less than %s hours",
                    TIME_LAG_FOR_PUBLISH_NEW_EVENT), "For the requested operation the conditions are not met");
        }
    }
}
