package ru.practicum.mainservice.request.dto;

import lombok.*;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private String created;

    @NotNull
    private Long event;

    @NotNull
    private long id;

    @NotNull
    private Long requester;

    @NotNull
    private Status status;
}