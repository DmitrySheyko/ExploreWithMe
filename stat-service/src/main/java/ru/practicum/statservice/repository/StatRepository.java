package ru.practicum.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statservice.model.EndPointHit;
import ru.practicum.statservice.model.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndPointHit, Long> {

    @Query("SELECT new ru.practicum.statservice.model.StatsResponse(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndPointHit AS e " +
            "WHERE e.timeStamp BETWEEN :startRange AND :endRange " +
            "GROUP BY e.app, e.uri, e.timeStamp " +
            "ORDER BY e.timeStamp ")
    List<StatsResponse> findByPeriod(LocalDateTime startRange, LocalDateTime endRange);

    @Query("SELECT new ru.practicum.statservice.model.StatsResponse(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndPointHit AS e " +
            "WHERE e.timeStamp BETWEEN :startRange AND :endRange " +
            "AND e.uri IN :uris " +
            "GROUP BY  e.app, e.uri, e.timeStamp " +
            "ORDER BY e.timeStamp ")
    List<StatsResponse> findByPeriodAndUris(LocalDateTime startRange, LocalDateTime endRange, List<String> uris);

    @Query("SELECT new ru.practicum.statservice.model.StatsResponse(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndPointHit AS e " +
            "WHERE e.timeStamp BETWEEN :startRange AND :endRange " +
            "GROUP BY  e.app, e.uri, e.timeStamp " +
            "ORDER BY e.timeStamp ")
    List<StatsResponse> findByPeriodAndUnique(LocalDateTime startRange, LocalDateTime endRange);


    @Query("SELECT new ru.practicum.statservice.model.StatsResponse(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndPointHit AS e " +
            "WHERE e.timeStamp BETWEEN :startRange AND :endRange " +
            "AND e.uri IN :uris " +
            "GROUP BY  e.app, e.uri, e.timeStamp " +
            "ORDER BY e.timeStamp ")
    List<StatsResponse> findByPeriodAndUrisAndUnique(LocalDateTime startRange, LocalDateTime endRange, List<String> uris);
}