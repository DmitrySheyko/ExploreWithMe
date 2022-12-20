package ru.practicum.mainservice.event.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.AdminUpdateEventDto;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.service.adminService.EventAdminService;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventAdminService service;

//    @GetMapping
//    public List<EventFullDto> search(@RequestParam("users") Integer[] users,
//                                     @RequestParam("states") String[] states,
//                                     @RequestParam("categories") Integer[] categories,
//                                     @RequestParam("rangeStart") String rangeStart,
//                                     @RequestParam("rangeEnd") String rangeEnd,
//                                     @RequestParam("from") Integer from,
//                                     @RequestParam("size") Integer size) {
//        return service.serach(users, states, categories, rangeStart, rangeEnd, from,size);
//    }

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

    @PatchMapping("/{eventId}/publish")
    public EventFullDto reject (@PathVariable("eventId") Long eventId){
        return service.reject(eventId);
    }
}
