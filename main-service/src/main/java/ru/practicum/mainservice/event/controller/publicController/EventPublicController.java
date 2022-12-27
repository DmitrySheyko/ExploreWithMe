package ru.practicum.mainservice.event.controller.publicController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.EventClient;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventPublicSearchDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.EventSearchSort;
import ru.practicum.mainservice.event.service.publicService.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {
    EventPublicService service;
    EventClient eventClient;

    @GetMapping
    public List<EventShortDto> search(@RequestParam(value = "text", required = false) @NotBlank String text,
                                      @RequestParam(value = "categories", required = false) List<Long> categories,
                                      @RequestParam(value = "paid", required = false, defaultValue = "false") Boolean paid,
                                      @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                      @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                      @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(value = "sort", required = false, defaultValue = "EVENT_DATE") String sort,
                                      @Valid @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                      @Valid @RequestParam(value = "size", required = false, defaultValue = "1") @Min(1) Integer size,
                                      HttpServletRequest request) {
        EventPublicSearchDto searchDto = EventPublicSearchDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .build();
        log.info("Получен запрос паблик эвент search");
        eventClient.addEndPointHit(request.getRemoteAddr(), request.getRequestURI(), LocalDateTime.now());
        return service.search(searchDto, from, size, EventSearchSort.valueOf(sort));
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable("id") Long eventId, HttpServletRequest request) {
        eventClient.addEndPointHit(request.getRemoteAddr(), request.getRequestURI(), LocalDateTime.now());
        log.info("Получен запрос паблик эвент getById");
        return service.getById(eventId);
    }
}