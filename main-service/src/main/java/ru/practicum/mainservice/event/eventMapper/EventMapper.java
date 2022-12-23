package ru.practicum.mainservice.event.eventMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.service.LocationService;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final RequestService requestService;
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
//                .views(null)
                .build();
    }

    public Event toEvent(PrivateUpdateEventDto privateUpdateEventDto) {
        return Event.builder()
                .annotation(privateUpdateEventDto.getAnnotation())
                .category(privateUpdateEventDto.getCategory())
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
//                .views(null)
                .build();
    }

    public Event toEvent(AdminUpdateEventDto adminUpdateEventDto) {
        return Event.builder()
                .annotation(adminUpdateEventDto.getAnnotation())
                .category(adminUpdateEventDto.getCategory())
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
//                .views(null)
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto e = new EventFullDto();
        e.setAnnotation(event.getAnnotation());
        e.setCategory(categoryMapper.toDto(event.getCategory()));
        e.setConfirmedRequests(requestService.getConfirmedRequests(event));
        e.setDescription(event.getDescription());
        e.setCreatedOn(dateTimeToString(event.getCreatedOn()));
        e.setEventDate(dateTimeToString(event.getEventDate()));
        e.setId(event.getId());
        e.setInitiator(userMapper.toShortDto(event.getInitiator()));
        e.setLocation(event.getLocation());
        e.setPaid(event.getPaid());
        e.setParticipantLimit(event.getParticipantLimit());
        e.setPublishedOn(dateTimeToString(event.getPublishedOn()));
        e.setRequestModeration(event.getRequestModeration());
        e.setState(event.getState());
        e.setTitle(event.getTitle());
//        return EventFullDto.builder()
//                .annotation(event.getAnnotation())
//                .category(categoryMapper.toDto(event.getCategory()))
//                .confirmedRequests(requestService.getConfirmedRequests(event.getId()))
//                .createdOn(dateTimeToString(event.getCreatedOn()))
//                .description(event.getDescription())
//                .eventDate(dateTimeToString(event.getEventDate()))
//                .id(event.getId())
//                .initiator(userMapper.toShortDto(event.getInitiator()))
//                .location(event.getLocation())
//                .paid(event.getPaid())
//                .participantLimit(event.getParticipantLimit())
//                .publishedOn(dateTimeToString(event.getPublishedOn()))
//                .requestModeration(event.getRequestModeration())
//                .state(event.getState())
//                .title(event.getTitle())
////                .views(event.getViews())
//                .build();
        return e;
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
//                .views(event.getViews())
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

    private Integer[] stateToInt(String[] states) {
        if(states == null || states.length==0){
            return null;
        }
//        Integer[] result = new Integer[states.length];
//        for (int i = 0; i < states.length; i++) {
//            String stringState = states[i];
//            State state = State.valueOf(stringState);
//            int intState = state.ordinal();
//            result[i] = intState;
//        }
//        return result;
        return Arrays.stream(states).map(stingState -> State.valueOf(stingState).ordinal()).toArray(Integer[]::new);
    }
}
