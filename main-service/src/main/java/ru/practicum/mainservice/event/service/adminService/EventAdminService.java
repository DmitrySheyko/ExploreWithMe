package ru.practicum.mainservice.event.service.adminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventAdminService {
    private final EventService service;
    CategoryService categoryService;
    private final EventMapper mapper;

    public EventFullDto update(AdminUpdateEventDto eventDto) {
        service.checkIsObjectInStorage(eventDto.getEventId());
        Event event = mapper.toEvent(eventDto);
        event = service.update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully updated", event.getId());
        return eventFullDto;
    }

    public EventFullDto publish(Long eventId) {
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if(! event.getState().equals(State.PENDING)){
            throw new ValidationException("For publication event should be in status PENDING",
                    "For the requested operation the conditions are not met");
        }
        service.checkEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event = service.update(event);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully published", event.getId());
        return eventFullDto;
    }

    public EventFullDto reject(Long eventId) {
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        if(event.getState().equals(State.PUBLISHED)){
            throw new ValidationException("Published event can't be rejected",
                    "For the requested operation the conditions are not met");
        }
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = mapper.toFullDto(event);
        log.info("Event eventId={} successfully canceled", event.getId());
        return eventFullDto;
    }

    public List<EventFullDto> search(EventAdminSearchDto searchDto, int from, int size){
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        EventAdminSearch eventAdminSearch = mapper.toEventAdminSearch(searchDto);
        if (eventAdminSearch.getCategories() == null) {
            List<Long> categoriesList = categoryService.findAll().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            eventAdminSearch.setCategories(categoriesList);
        }
        if(eventAdminSearch.getStates() == null) {
            List<Integer> statesList = Arrays.stream(State.values())
                    .map(Enum::ordinal)
                    .collect(Collectors.toList());
            eventAdminSearch.setStates(statesList);
        }
        if (eventAdminSearch.getRangeStart() == null) {
            eventAdminSearch.setRangeStart(LocalDateTime.now());
        }
        if (eventAdminSearch.getRangeEnd() == null) {
            eventAdminSearch.setRangeStart(LocalDateTime.MAX);
        }
        Page<Event> eventsPage;
        if (eventAdminSearch.getUsers() == null) {
            eventsPage = service.searchByUsersSet(eventAdminSearch, pageable);
        } else {
            eventsPage = service.searchForAllUsers(eventAdminSearch, pageable);
        }
        List<EventFullDto> eventDtoList = eventsPage.stream().map(mapper::toFullDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        return eventDtoList;
    }
}
