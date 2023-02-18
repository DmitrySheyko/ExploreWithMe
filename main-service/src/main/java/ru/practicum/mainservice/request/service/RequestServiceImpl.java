package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation service class for {@link Request} entity
 * Implements interface {@link RequestService}
 *
 * @author DmitrySheyko
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException((String.format("User with id=%s was not found.", userId))));
        if (repository.findByRequesterAndEventId(requester, eventId).isPresent()) {
            throw new ValidationException(String.format("Request from user id=%s, to event id==%s already exist", userId,
                    eventId));
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", eventId))));
        checkTermsForNewRequest(requester, event);
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .build();
        if (event.getRequestModeration()) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
        }
        request = repository.save(request);
        ParticipationRequestDto requestDto = RequestMapper.toDto(request);
        log.info("Created participation request id={} from user id={} for event id{}", request.getId(), userId, eventId);
        return requestDto;
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException((String.format("User with id=%s was not found.", userId))));
        Request request = repository.findByIdAndRequester(requestId, user)
                .orElseThrow(() -> new NotFoundException(String.format("Request from user id=%s to event=%s not found",
                        userId, requestId)));
        request.setStatus(Status.CANCELED);
        request = repository.save(request);
        ParticipationRequestDto requestDto = RequestMapper.toDto(request);
        log.info("Request id={} successfully canceled", requestId);
        return requestDto;
    }

    @Override
    public List<ParticipationRequestDto> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException((String.format("User with id=%s was not found.", userId))));
        List<Request> requestsList = repository.findAllByRequesterOrderById(user);
        List<ParticipationRequestDto> requestDtoList = requestsList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
        log.info("Requests of user id={} successfully received", userId);
        return requestDtoList;
    }

    private void checkTermsForNewRequest(User requester, Event event) {
        if (event.getInitiator().equals(requester)) {
            throw new ValidationException("Initiator of event can't create participation requests. Request didn't " +
                    "created");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Request can be created only for published status events. Request didn't " +
                    "created");
        }
        if (event.getParticipantLimit() != 0 && event.getRequestsSet().stream()
                .filter(request -> request.getStatus().equals(Status.CONFIRMED))
                .collect(Collectors.toSet()).size() >= event.getParticipantLimit()) {
            throw new ValidationException("Participants limit is full. Request didn't created");
        }
    }
}