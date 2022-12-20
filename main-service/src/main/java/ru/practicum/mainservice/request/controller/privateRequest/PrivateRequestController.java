package ru.practicum.mainservice.request.controller.privateRequest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.service.privareRequest.PrivateRequestService;

@RestController
@RequestMapping("/users/{userId}/request")
@AllArgsConstructor
public class PrivateRequestController {
    PrivateRequestService service;

    @PostMapping
    public ParticipationRequestDto add (@PathVariable ("userId") Long userId,
                                        @RequestParam("eventId") Long eventId){
        return service.add(userId, eventId);
    }
}
