package ru.practicum.mainservice.request.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.service.RequestService;

import java.util.List;

/**
 * Class of controller for {@link Request} entity for users with User role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final RequestService service;

    @PostMapping
    public ParticipationRequestDto add(@PathVariable(value = "userId") Long userId,
                                       @RequestParam(value = "eventId") Long eventId) {
        return service.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable("userId") Long userId,
                                          @PathVariable("requestId") Long requestId) {
        return service.cancel(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllByUserId(@PathVariable("userId") Long userId) {
        return service.getAllByUserId(userId);
    }
}