package ru.practicum.mainservice.event.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.dto.AdminUpdateEventDto;
import ru.practicum.mainservice.event.dto.EventAdminSearchDto;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.service.adminService.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventAdminService service;

    @GetMapping
    public List<EventFullDto> search(@RequestParam(value = "users", required = false) List<Long> users,
                                     @RequestParam(value = "states", required = false) List<String> states,
                                     @RequestParam(value = "categories", required = false) List<Long> categories,
                                     @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                     @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                     @Valid @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                     @Valid @RequestParam(value = "size", required = false, defaultValue = "1") @Min(1) Integer size) {
        EventAdminSearchDto searchDto = EventAdminSearchDto.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return service.search(searchDto, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Long eventId,
                               @RequestBody AdminUpdateEventDto eventDto) {
        eventDto.setEventId(eventId);
        return service.update(eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish (@PathVariable("eventId") Long eventId){
        return service.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject (@PathVariable("eventId") Long eventId){
        return service.reject(eventId);
    }
}
