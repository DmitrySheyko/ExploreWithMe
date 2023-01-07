package ru.practicum.mainservice.event.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.AdminUpdateEventDto;
import ru.practicum.mainservice.event.dto.EventAdminSearchDto;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> search(@RequestParam(value = "users", required = false) Set<Long> users,
                                     @RequestParam(value = "states", required = false) Set<String> states,
                                     @RequestParam(value = "categories", required = false) Set<Long> categories,
                                     @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                     @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                     @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                     @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        EventAdminSearchDto searchDto = EventAdminSearchDto.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return service.adminSearch(searchDto, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Long eventId,
                               @RequestBody AdminUpdateEventDto eventDto) {
        eventDto.setEventId(eventId);
        return service.update(eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable("eventId") Long eventId) {
        return service.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable("eventId") Long eventId) {
        return service.reject(eventId);
    }
}