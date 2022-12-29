package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long InitiatorId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', :text, '%')))) " +
            "AND e.category.id IN :categories " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND e.publishedOn <> null " +
            "AND e.participantLimit > (SELECT COUNT(r.id) " +
            "FROM Request r " +
            "WHERE r.status = :status " +
            "GROUP BY r.id) ")
    Page<Event> searchAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Integer status, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "AND e.publishedOn <> null ")
    Page<Event> searchAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id IN :users " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd ")
    Page<Event> searchByUsersSet(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd ")
    Page<Event> searchForAllUsers(List<State> states, List<Long> categories, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Pageable pageable);
}