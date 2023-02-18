package ru.practicum.mainservice.request.dto;

import lombok.*;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.constraints.NotNull;

/**
 * Class of dto for {@link Request}
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private String created;

    @NotNull(message = "Event id of request shouldn't be null")
    private Long event;

    @NotNull(message = "Id of request shouldn't be null")
    private long id;

    @NotNull(message = "Requester id shouldn't be null")
    private Long requester;

    @NotNull(message = "Status of request shouldn't be null")
    private Status status;
}