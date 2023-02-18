package ru.practicum.mainservice.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper class for {@link Request} entity
 *
 * @author DmitrySheyko
 */
@Component
public class RequestMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(dateTimeToString(request.getCreated()))
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    private static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_PATTERN);
    }
}