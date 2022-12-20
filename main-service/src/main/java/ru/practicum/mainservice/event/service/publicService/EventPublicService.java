package ru.practicum.mainservice.event.service.publicService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventPublicSearchDto;
import ru.practicum.mainservice.event.dto.EventPublicSearch;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventPublicService {
    EventService service;
    CategoryService categoryService;
    EventMapper mapper;

    //TODO сделать енум возможных полей для сортировки
    public List<EventShortDto> search(EventPublicSearchDto searchDto, int from, int size, String sort) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        EventPublicSearch eventPublicSearch = mapper.toEventPublicSearch(searchDto);
        if (eventPublicSearch.getCategories() == null) {
            Long[] categoriesArray = categoryService.findAll().stream().map(Category::getId).toArray(Long[]::new);
            eventPublicSearch.setCategories(categoriesArray);
        }
        if (eventPublicSearch.getRangeStart() == null) {
            eventPublicSearch.setRangeStart(LocalDateTime.now());
        }
        if (eventPublicSearch.getRangeEnd() == null) {
            eventPublicSearch.setRangeStart(LocalDateTime.MAX);
        }
        Page<Event> eventsPage;
        if (eventPublicSearch.getOnlyAvailable()) {
            eventsPage = service.searchAvailable(eventPublicSearch, pageable);
        } else {
            eventsPage = service.searchAll(eventPublicSearch, pageable);
        }
        List<EventShortDto> eventDtoList = eventsPage.stream().map(mapper::toShortDto).collect(Collectors.toList());
        log.info("List of searched events successfully received");
        return eventDtoList;
    }


    //TODO добавить проверку что событие опубликовано
    public EventFullDto getById(Long eventId) {
        service.checkIsObjectInStorage(eventId);
        Event event = service.findById(eventId);
        EventFullDto eventDto = mapper.toFullDto(event);
        log.info("Event id={} successfully received", eventDto);
        return eventDto;
    }
}