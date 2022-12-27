package ru.practicum.mainservice.event.eventMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.EventClient;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.service.LocationService;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final RequestService requestService;
    private final EventClient eventClient;
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(categoryService.getById(newEventDto.getCategory()))
                .createdOn(null)
                .description(newEventDto.getDescription())
                .eventDate(toLocalDateTime(newEventDto.getEventDate()))
                .initiator(null)
                .location(locationService.save(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(null)
                .title(newEventDto.getTitle())
                .build();
    }

    public Event toEvent(PrivateUpdateEventDto updateEventDto) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation())
                .category(categoryService.getById(updateEventDto.getCategory()))
                .createdOn(null)
                .description(updateEventDto.getDescription())
                .eventDate(toLocalDateTime(updateEventDto.getEventDate()))
                .id(updateEventDto.getEventId())
                .initiator(null)
                .location(null)
                .paid(updateEventDto.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit())
                .publishedOn(null)
                .state(null)
                .title(updateEventDto.getTitle())
                .build();
    }

    public Event toEvent(AdminUpdateEventDto adminUpdateEventDto) {
        return Event.builder()
                .annotation(adminUpdateEventDto.getAnnotation())
                .category(categoryService.getById(adminUpdateEventDto.getCategory()))
                .createdOn(null)
                .description(adminUpdateEventDto.getDescription())
                .eventDate(toLocalDateTime(adminUpdateEventDto.getEventDate()))
                .id(adminUpdateEventDto.getEventId())
                .initiator(null)
                .location(adminUpdateEventDto.getLocation())
                .participantLimit(adminUpdateEventDto.getParticipantLimit())
                .paid(adminUpdateEventDto.getPaid())
                .publishedOn(null)
                .requestModeration(adminUpdateEventDto.getRequestModeration())
                .state(null)
                .title(adminUpdateEventDto.getTitle())
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(requestService.getConfirmedRequests(event))
                .createdOn(dateTimeToString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(dateTimeToString(event.getEventDate()))
                .id(event.getId())
                .initiator(userMapper.toShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(dateTimeToString(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(requestService.getConfirmedRequests(event))
                .eventDate(dateTimeToString(event.getEventDate()))
                .id(event.getId())
                .initiator(userMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public EventPublicSearch toEventPublicSearch(EventPublicSearchDto searchDto) {
        return EventPublicSearch.builder()
                .categories(searchDto.getCategories())
                .onlyAvailable(searchDto.getOnlyAvailable())
                .paid(searchDto.getPaid())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .text(searchDto.getText().toUpperCase())
                .build();
    }

    public EventAdminSearch toEventAdminSearch(EventAdminSearchDto searchDto) {
        return EventAdminSearch.builder()
                .users(searchDto.getUsers())
                .states(stateToInt(searchDto.getStates()))
                .categories(searchDto.getCategories())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .build();
    }

    private String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_PATTERN);
    }

    private LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }

    private List<State> stateToInt(List<String> states) {
        if (states == null || states.size() == 0) {
            return null;
        }
        return states.stream()
                .map(State::valueOf)
                .collect(Collectors.toList());
    }

    private Integer getViews(Long eventId) {
        return eventClient.getViews(eventId);
    }
}