package ru.practicum.mainservice.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateUpdateEventDto {

    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    @NotNull
    private Long eventId;

    private Boolean paid;

    private Integer participantLimit;

    @Length(min = 3, max = 120)
    private String title;
}