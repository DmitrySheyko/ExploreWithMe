package ru.practicum.mainservice.request.service.privareRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PrivateRequestService {
    RequestService service;
    UserService userService;
    EventService eventService;
    RequestMapper mapper;


    public ParticipationRequestDto add(Long userId, Long eventId) {
        userService.checkIsObjectInStorage(userId);
        eventService.checkIsObjectInStorage(eventId);
        Event event = eventService.findById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            log.warn("Initiator of event can't create participation requests. Request didn't created");
            throw new ValidationException("Initiator of event can't create participation requests. Request didn't " +
                    "created", "For the requested operation the conditions are not met.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("Request can be created only for published status events. Request didn't created");
            throw new ValidationException("Request can be created only for published status events. Request didn't " +
                    "created", "For the requested operation the conditions are not met.");
        }
        if (event.getRequestsSet().stream()
                .filter(request -> request.getStatus().equals(Status.APPROVED))
                .collect(Collectors.toSet()).size() >= event.getParticipantLimit()) {
            log.warn("Participants limit is full. Request didn't created");
            throw new ValidationException("Participants limit is full. Request didn't " +
                    "created", "For the requested operation the conditions are not met.");
        }
        if (event.getRequestsSet().stream()
                .anyMatch(request -> request.getRequester().equals(userId))) {
            log.warn("User id={} already participant of this event . Request didn't created", userId);
            throw new ValidationException(String.format("User id=%s already participant of this event. " +
                    "Request didn't created", userId), "For the requested operation the conditions are not met.");
        }
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(userId)
                .build();
        if(event.getRequestModeration()){
            request.setStatus(Status.PENDING);
            request = service.save(request);
        } else {
            request.setStatus(Status.APPROVED);
            request = service.save(request);
        }
        ParticipationRequestDto requestDto = mapper.toDto(request);
        log.info("Participation request id={} from user id={} for event id{}", request.getId(), userId, eventId);
        return requestDto;
    }
}
