package ru.practicum.statservice.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statservice.Dto.NewEndPointHitDto;
import ru.practicum.statservice.Dto.StatRequestDto;
import ru.practicum.statservice.Dto.StatsRequest;
import ru.practicum.statservice.model.EndPointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndPointHit toEndPointHit(NewEndPointHitDto dto) {
        return EndPointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timeStamp(toLocalDateTime(dto.getTimeStamp()))
                .build();
    }

    public StatsRequest toStatRequest(StatRequestDto dto) {
        return StatsRequest.builder()
                .start(toLocalDateTime(dto.getStart()))
                .end(toLocalDateTime(dto.getEnd()))
                .uris(dto.getUris())
                .unique(dto.getUnique())
                .build();
    }

    private String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_PATTERN);
    }

    private LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }
}