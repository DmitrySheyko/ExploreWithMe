package ru.practicum.mainservice.event.eventMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(newEventDto.getCategory())
                .confirmedRequests(null)
                .createdOn(null)
                .description(newEventDto.getDescription())
                .eventDate(toLocalDateTime(newEventDto.getEventDate()))
                .initiator(null)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(null)
                .title(newEventDto.getTitle())
                .views(null)
                .build();
    }

    public Event toEvent(PrivateUpdateEventDto privateUpdateEventDto) {
        return Event.builder()
                .annotation(privateUpdateEventDto.getAnnotation())
                .category(privateUpdateEventDto.getCategory())
                .confirmedRequests(null)
                .createdOn(null)
                .description(privateUpdateEventDto.getDescription())
                .eventDate(toLocalDateTime(privateUpdateEventDto.getEventDate()))
                .id(privateUpdateEventDto.getEventId())
                .initiator(null)
                .location(null)
                .paid(privateUpdateEventDto.getPaid())
                .participantLimit(privateUpdateEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(privateUpdateEventDto.getRequestModeration())
                .state(null)
                .title(privateUpdateEventDto.getTitle())
                .views(null)
                .build();
    }

    public Event toEvent(AdminUpdateEventDto adminUpdateEventDto) {
        return Event.builder()
                .annotation(adminUpdateEventDto.getAnnotation())
                .category(adminUpdateEventDto.getCategory())
                .confirmedRequests(null)
                .createdOn(null)
                .description(adminUpdateEventDto.getDescription())
                .eventDate(toLocalDateTime(adminUpdateEventDto.getEventDate()))
                .id(adminUpdateEventDto.getEventId())
                .initiator(null)
                .location(adminUpdateEventDto.getLocation())
                .paid(adminUpdateEventDto.getPaid())
                .participantLimit(adminUpdateEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(adminUpdateEventDto.getRequestModeration())
                .state(null)
                .title(adminUpdateEventDto.getTitle())
                .views(null)
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
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
                .views(event.getViews())
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(dateTimeToString(event.getEventDate()))
                .id(event.getId())
                .initiator(userMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventPublicSearch toEventPublicSearch(EventPublicSearchDto searchDto){
        return EventPublicSearch.builder()
                .categories(searchDto.getCategories())
                .onlyAvailable(searchDto.getOnlyAvailable())
                .paid(searchDto.getPaid())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .text(searchDto.getText().toUpperCase())
                .build();
    }

    public EventAdminSearch toEventPublicSearch(EventAdminSearchDto searchDto){
        return EventAdminSearch.builder()
                .users(searchDto.getUsers())
                .states(searchDto.getStates())
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
}
