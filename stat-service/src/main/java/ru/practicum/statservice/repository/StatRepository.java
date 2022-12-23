package ru.practicum.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponce;

import java.time.LocalDateTime;

public interface StatRepository extends JpaRepository<EndPointHit, Long> {

    @Query(value = "SELECT s.application, " +
            "s.uri, " +
            "COUNT(s.ip) as hits " +
            "FROM statistics s" +
            "WHERE s.request_time >= ?" +
            "AND s.request_time <= ?" +
            "GROUP BY s.ip " +
            "ORDER BY request_time", nativeQuery = true)
    StatsResponce findByPeriod(LocalDateTime start, LocalDateTime end);


    @Query(value = "SELECT s.application, " +
            "s.uri, " +
            "COUNT(s.ip) as hits " +
            "FROM statistics s" +
            "WHERE s.request_time >= ?" +
            "AND s.request_time <= ?" +
            "AND s.uri IN (?)" +
            "GROUP BY s.ip " +
            "ORDER BY request_time", nativeQuery = true)
    StatsResponce findByPeriodAndUris(LocalDateTime start, LocalDateTime end, String[] uris);


    @Query(value = "SELECT s.application, " +
            "s.uri, " +
            "COUNT(DISTINCT s.ip) as hits " +
            "FROM statistics s" +
            "WHERE s.request_time >= ?" +
            "AND s.request_time <= ?" +
            "GROUP BY s.ip " +
            "ORDER BY request_time", nativeQuery = true)
    StatsResponce findByPeriodAndUnique(LocalDateTime start, LocalDateTime end, Boolean unique);


    @Query(value = "SELECT s.application, " +
            "s.uri, " +
            "COUNT(DISTINCT s.ip) as hits " +
            "FROM statistics s" +
            "WHERE s.request_time >= ?" +
            "AND s.request_time <= ?" +
            "AND s.uri IN (?)" +
            "GROUP BY s.ip " +
            "ORDER BY request_time", nativeQuery = true)
    StatsResponce findByPeriodAndUrisAndUnique(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}