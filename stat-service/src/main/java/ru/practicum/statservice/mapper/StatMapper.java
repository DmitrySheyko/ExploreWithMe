package ru.practicum.statservice.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statservice.dto.NewEndPointHitDto;
import ru.practicum.statservice.dto.StatRequestDto;
import ru.practicum.statservice.dto.StatsRequest;
import ru.practicum.statservice.model.EndPointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndPointHit toEndPointHit(NewEndPointHitDto dto) {
        return EndPointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timeStamp(toLocalDateTime(dto.getTimeStamp()))
                .build();
    }

    public static StatsRequest toStatRequest(StatRequestDto dto) {
        return StatsRequest.builder()
                .start(toLocalDateTime(dto.getStart()))
                .end(toLocalDateTime(dto.getEnd()))
                .uris(dto.getUris())
                .unique(dto.getUnique())
                .build();
    }

    private static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }
}