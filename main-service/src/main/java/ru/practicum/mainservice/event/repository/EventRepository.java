package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * Interface of JpaRepository for {@link Event} entity.
 *
 * @author DmitrySheyko
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.category.id IN (?2) " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "AND e.publishedOn > ?4 " +
            "AND e.participantLimit > (SELECT COUNT(r.id) " +
            "FROM Request r " +
            "WHERE r.status = ?6 " +
            "GROUP BY r.id) ")
    Page<Event> searchAvailable(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Integer status, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "AND e.publishedOn > ?4 ")
    Page<Event> searchAll(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.category.id IN (?3) " +
            "AND e.eventDate BETWEEN ?4 AND ?5 ")
    Page<Event> searchByUsersSet(Set<Long> users, Set<State> states, Set<Long> categories, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.state IN (?1) " +
            "AND e.category.id IN (?2) " +
            "AND e.eventDate BETWEEN ?3 AND ?4 ")
    Page<Event> searchForAllUsers(Set<State> states, Set<Long> categories, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}