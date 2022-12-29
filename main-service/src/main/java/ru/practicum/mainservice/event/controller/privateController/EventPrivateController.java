package ru.practicum.mainservice.event.controller.privateController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.PrivateUpdateEventDto;
import ru.practicum.mainservice.event.service.EventServiceImpl;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventServiceImpl service;

    @PostMapping
    public EventFullDto add(@PathVariable("userId") Long userId,
                            @Valid @RequestBody NewEventDto newEventDto) {
        return service.add(userId, newEventDto);
    }

    @PatchMapping
    public EventFullDto update(@PathVariable("userId") Long userId,
                               @RequestBody PrivateUpdateEventDto privateUpdateEventDto) {
        return service.update(userId, privateUpdateEventDto);
    }

    @GetMapping
    public List<EventShortDto> getAllByUserId(@PathVariable("userId") Long userId,
                                              @RequestParam(name = "from", required = false, defaultValue = "0")
                                              @Min(0) int from,
                                              @RequestParam(name = "size", required = false, defaultValue = "10")
                                              @Min(1) int size) {
        return service.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        return service.getById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        return service.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsToEventsOfUser(@PathVariable("userId") Long userId,
                                                                   @PathVariable("eventId") Long eventId) {
        return service.getRequestsToEventsOfUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(@PathVariable("userId") Long userId,
                                                               @PathVariable("eventId") Long eventId,
                                                               @PathVariable("reqId") Long requestId) {
        return service.changeStatusOfParticipationRequest(userId, eventId, requestId, Status.CONFIRMED);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequest(@PathVariable("userId") Long userId,
                                                              @PathVariable("eventId") Long eventId,
                                                              @PathVariable("reqId") Long requestId) {
        return service.changeStatusOfParticipationRequest(userId, eventId, requestId, Status.REJECTED);
    }
}