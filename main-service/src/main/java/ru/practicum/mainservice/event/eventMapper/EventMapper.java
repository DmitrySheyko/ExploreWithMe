package ru.practicum.mainservice.event.eventMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.client.EventClient;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto, Category category, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(null)
                .description(newEventDto.getDescription())
                .eventDate(toLocalDateTime(newEventDto.getEventDate()))
                .initiator(null)
                .location(location)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(null)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event toEvent(PrivateUpdateEventDto updateEventDto, Category category) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation())
                .category(category)
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

    public static Event toEvent(AdminUpdateEventDto adminUpdateEventDto, Category category) {
        return Event.builder()
                .annotation(adminUpdateEventDto.getAnnotation())
                .category(category)
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

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(getConfirmedRequests(event.getRequestsSet()))
                .createdOn(dateTimeToString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(dateTimeToString(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
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

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(getConfirmedRequests(event.getRequestsSet()))
                .eventDate(dateTimeToString(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    public static EventPublicSearch toEventPublicSearch(EventPublicSearchDto searchDto) {
        return EventPublicSearch.builder()
                .categories(searchDto.getCategories())
                .onlyAvailable(searchDto.getOnlyAvailable())
                .paid(searchDto.getPaid())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .text(searchDto.getText())
                .build();
    }

    public static EventAdminSearch toEventAdminSearch(EventAdminSearchDto searchDto) {
        return EventAdminSearch.builder()
                .users(searchDto.getUsers())
                .states(stateToInt(searchDto.getStates()))
                .categories(searchDto.getCategories())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .build();
    }

    private static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_PATTERN);
    }

    private static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }

    private static Set<State> stateToInt(Set<String> states) {
        if (states == null || states.size() == 0) {
            return null;
        }
        return states.stream()
                .map(State::valueOf)
                .collect(Collectors.toSet());
    }

    private static Integer getViews(Long eventId) {
        return EventClient.getViews(eventId);
    }

    private static Integer getConfirmedRequests(List<Request> requestList) {
        if (requestList == null || requestList.isEmpty()) {
            return 0;
        }
        return requestList.stream()
                .filter(request -> request.getStatus().equals(Status.CONFIRMED))
                .collect(Collectors.toSet())
                .size();
    }
}