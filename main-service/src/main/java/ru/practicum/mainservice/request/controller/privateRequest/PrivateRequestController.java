package ru.practicum.mainservice.request.controller.privateRequest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.service.privareRequest.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class PrivateRequestController {
    PrivateRequestService service;

    @PostMapping
    public ParticipationRequestDto add(@PathVariable(value = "userId", required = true) Long userId,
                                       @RequestParam(value = "eventId", required = true) Long eventId) {
        return service.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable("userId") Long userId,
                                          @RequestParam("requestId") Long requestId) {
        return service.cancel(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllByUserId(@PathVariable("userId") Long userId) {
        return service.getAllByUserId(userId);
    }
}
