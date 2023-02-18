package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface of JpaRepository for {@link Request} entity
 *
 * @author DmitrySheyko
 */
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterOrderById(User userId);

    List<Request> findAllByEvent(Event event);

    Optional<Request> findByIdAndRequester(Long requestId, User user);

    Optional<Request> findByRequesterAndEventId(User user, Long eventId);
}