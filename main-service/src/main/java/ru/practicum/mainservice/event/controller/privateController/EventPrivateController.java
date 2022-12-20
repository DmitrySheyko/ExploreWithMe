package ru.practicum.mainservice.event.controller.privateController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.PrivateUpdateEventDto;
import ru.practicum.mainservice.event.service.privateService.EventPrivateService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventPrivateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable("userId") Long userId,
                            @RequestBody NewEventDto newEventDto) {
        return service.add(userId, newEventDto);
    }

    @PatchMapping
    public EventFullDto update(@PathVariable("userId") Long userId,
                               @RequestBody PrivateUpdateEventDto privateUpdateEventDto) {
        return service.update(userId, privateUpdateEventDto);
    }

    @GetMapping
    public List<EventShortDto> getAllByUserId(@PathVariable("userId") Long userId,
                                              @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) int from,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) int size){
        return service.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{eventId")
    public EventFullDto getById (@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId){
        return service.getById(userId, eventId);
    }

    @PatchMapping("/{eventId")
    public EventFullDto cancelEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId){
        return service.cancelEvent(userId, eventId);
    }

//    GET
///users/{userId}/events/{eventId}/requests
//    Получение информации о запросах на участие в событии текущего пользователя
//    PATCH
///users/{userId}/events/{eventId}/requests/{reqId}/confirm
//    Подтверждение чужой заявки на участие в событии текущего пользователя
//            PATCH
///users/{userId}/events/{eventId}/requests/{reqId}/reject
//    Отклонение чужой заявки на участие в событии текущего пользователя
}
