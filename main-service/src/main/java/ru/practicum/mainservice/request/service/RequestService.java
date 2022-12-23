package ru.practicum.mainservice.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.repository.RequestRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RequestService {
    RequestRepository repository;

    public Request save(Request request) {
        return repository.save(request);
    }

    public Request findById(Long requestId) {
        Optional<Request> optionalRequest = repository.findById(requestId);
        if (optionalRequest.isPresent()) {
            return optionalRequest.get();
        }
        throw new NotFoundException((String.format("Request id=%s was not found.", requestId)),
                "The required object was not found.");
    }

    public List<Request> findAllByUserId(Long userId) {
        return repository.findAllByRequesterOrderById(userId);
    }

    public void checkIsObjectInStorage(Request request) {
        if (!repository.existsById(request.getId())) {
            throw new NotFoundException((String.format("Request with id=%s was not found.", request.getId())),
                    "The required object was not found.");
        }
    }

    public void checkIsObjectInStorage(Long requestId) {
        if (!repository.existsById(requestId)) {
            throw new NotFoundException((String.format("Request id=%s was not found.", requestId)),
                    "The required object was not found.");
        }
    }

    public Integer getConfirmedRequests(Event event) {
        List<Request> requestList = repository.findAllByEventAndStatus(event, Status.APPROVED);
        return requestList.size();
    }
}
