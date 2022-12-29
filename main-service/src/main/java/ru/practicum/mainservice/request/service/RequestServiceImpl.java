package ru.practicum.mainservice.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserServiceImpl userServiceImpl;
    private final RequestMapper mapper;
    private final RequestRepository repository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        Event event = findEventById(eventId);
        checkTermsForNewRequest(userId, event);
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(userId)
                .build();
        if (event.getRequestModeration()) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
        }
        request = repository.save(request);
        ParticipationRequestDto requestDto = mapper.toDto(request);
        log.info("Participation request id={} from user id={} for event id{}", request.getId(), userId, eventId);
        return requestDto;
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        checkIsObjectInStorage(requestId);
        Request request = findById(requestId);
        request.setStatus(Status.CANCELED);
        request = repository.save(request);
        ParticipationRequestDto requestDto = mapper.toDto(request);
        log.info("Request id={} successfully canceled", requestId);
        return requestDto;
    }

    @Override
    public List<ParticipationRequestDto> getAllByUserId(Long userId) {
        userServiceImpl.checkIsObjectInStorage(userId);
        List<Request> requestsList = repository.findAllByRequesterOrderById(userId);
        List<ParticipationRequestDto> requestDtoList = requestsList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.info("Requests of user id={} successfully received", userId);
        return requestDtoList;
    }

    @Override
    public Request findById(Long requestId) {
        Optional<Request> optionalRequest = repository.findById(requestId);
        if (optionalRequest.isPresent()) {
            return optionalRequest.get();
        }
        throw new NotFoundException((String.format("Request id=%s was not found.", requestId)));
    }

    @Override
    public List<Request> findAllByEvent(Event event) {
        return repository.findAllByEvent(event);
    }

    @Override
    public Request save(Request request) {
        return repository.save(request);
    }

    @Override
    public Event findEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new NotFoundException((String.format("Event with id=%s was not found.", eventId)));
    }

    @Override
    public void checkIsObjectInStorage(Long requestId) {
        if (!repository.existsById(requestId)) {
            throw new NotFoundException((String.format("Request id=%s was not found.", requestId)));
        }
    }

    private void checkTermsForNewRequest(Long userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            log.warn("Initiator of event can't create participation requests. Request didn't created");
            throw new ValidationException("Initiator of event can't create participation requests. Request didn't " +
                    "created");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("Request can be created only for published status events. Request didn't created");
            throw new ValidationException("Request can be created only for published status events. Request didn't " +
                    "created");
        }
        if (event.getParticipantLimit() != 0 && event.getRequestsSet().stream()
                .filter(request -> request.getStatus().equals(Status.CONFIRMED))
                .collect(Collectors.toSet()).size() >= event.getParticipantLimit()) {
            log.warn("Participants limit is full. Request didn't created");
            throw new ValidationException("Participants limit is full. Request didn't created");
        }
        if (event.getRequestsSet().stream()
                .anyMatch(request -> request.getRequester().equals(userId))) {
            log.warn("User id={} already participant of this event . Request didn't created", userId);
            throw new ValidationException(String.format("User id=%s already participant of this event. " +
                    "Request didn't created", userId));
        }
    }
}