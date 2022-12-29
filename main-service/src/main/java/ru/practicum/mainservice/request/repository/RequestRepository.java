package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterOrderById(Long userId);

    List<Request> findAllByEvent(Event event);
}