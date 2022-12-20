package ru.practicum.mainservice.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.repository.RequestRepository;

@Service
@AllArgsConstructor
public class RequestService {
    RequestRepository repository;

    public Request save(Request request) {
        return repository.save(request);
    }

}
