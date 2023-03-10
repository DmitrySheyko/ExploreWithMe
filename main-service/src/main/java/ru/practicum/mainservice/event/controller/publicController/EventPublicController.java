package ru.practicum.mainservice.event.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.client.EventClient;
import ru.practicum.mainservice.event.dto.EventAdminSearch;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventPublicSearchDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventSearchSort;
import ru.practicum.mainservice.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Class of dto for {@link EventAdminSearch} entity.
 * For searching {@link Event} by requests from users with public role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService service;
    private final EventClient eventClient;

    @GetMapping
    public List<EventShortDto> search(@RequestParam(value = "text", required = false) String text,
                                      @RequestParam(value = "categories", required = false) Set<Long> categories,
                                      @RequestParam(value = "paid", defaultValue = "false") Boolean paid,
                                      @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                      @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                      @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                      @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
                                      HttpServletRequest request) {
        EventPublicSearchDto searchDto = EventPublicSearchDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .build();
        eventClient.addEndPointHit(request.getRemoteAddr(), request.getRequestURI(), LocalDateTime.now());
        return service.publicSearch(searchDto, from, size, EventSearchSort.valueOf(sort));
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable("id") Long eventId, HttpServletRequest request) {
        eventClient.addEndPointHit(request.getRemoteAddr(), request.getRequestURI(), LocalDateTime.now());
        return service.getById(eventId);
    }
}