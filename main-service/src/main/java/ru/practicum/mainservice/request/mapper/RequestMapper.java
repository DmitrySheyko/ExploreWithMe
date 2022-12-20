package ru.practicum.mainservice.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester())
                .status(request.getStatus())
                .build();
    }
}
