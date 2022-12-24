package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long InitiatorId, Pageable pageable);


    @Query(value = "WITH participants AS " +
            "(SELECT COUNT(r.id) " +
            "FROM requests r " +
            "WHERE r.status = ?6 " +
            "GROUP BY r.id) " +
            "SELECT e.* " +
            "FROM events e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')))" +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '%'))))" +
            "AND e.category_id IN (?2)" +
            "AND e.is_paid = ?3 " +
            "AND (e.event_date <= ?4 AND e.event_date >= ?5) " +
            "AND e.participant_limit > participants " +
            "ORDER BY e.id ", nativeQuery = true)
    Page<Event> searchAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Integer status, Pageable pageable);

    @Query(value = "SELECT e.* " +
            "FROM events e " +
            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.category_id IN (?2) " +
            "AND e.is_paid = ?3 " +
            "AND (e.event_date <= ?4 AND e.event_date >= ?5) " , nativeQuery = true)
    Page<Event> searchAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, Pageable pageable);


    @Query(value = "SELECT * " +
            "FROM events e " +
            "WHERE e.initiator_id IN (?1) " +
            "AND e.state IN (?2) " +
            "AND e.category_id IN (?3)" +
            "AND (e.event_date >= ?4 AND e.event_date <= ?5) " +
            "ORDER BY e.id ", nativeQuery = true)
    Page<Event> searchByUsersSet(List<Long> users, List<Integer> states, List<Long> categories, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, Pageable pageable);



    @Query(value = "SELECT * " +
            "FROM events e " +
            "WHERE e.state IN (?1) " +
            "AND e.category_id IN (?2)" +
            "AND (e.event_date >= ?3 AND e.event_date <= ?4) " +
            "ORDER BY e.id ", nativeQuery = true)
    Page<Event> searchForAllUsers(List<Integer> states, List<Long> categories, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Pageable pageable);
}


//    @Query("WITH participants AS" +
//            "SELECT COUNT(r.id) " +
//            "FROM requests r " +
//            "WHERE r.status = ?6 " +
//            "GROUP BY r.id " +
//            "SELECT e " +
//            "FROM events e LEFT JOIN requsts r" +
//            "on e.id = r.event " +
//            "WHERE ((UPPER (e.annotation) LIKE UPPER(CONCAT('%', ?1, '1')))" +
//            "OR (UPPER (e.description) LIKE UPPER(CONCAT('%', ?1, '1'))))" +
//            "AND e.category in (?2)" +
//            "AND e.paid = ?3 " +
//            "AND (e.eventDate >= ?4 AND e.eventDate <= ?5) " +
//            "AND e.participantLimit > participants " +
//            "SORT BY e.id ")
//    Page<Event> search(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
//                       LocalDateTime rangeEnd, Integer status, Pageable pageable);