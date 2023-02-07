package ru.practicum.mainservice.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateUpdateEventDto {

    @Length(min = 20, max = 2000, message = "Annotation length shouldn't be min=20, max=2000")
    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    @NotNull(message = "eventId shouldn't be null")
    private Long eventId;

    private Boolean paid;

    private Integer participantLimit;

    @Length(min = 3, max = 120, message = "Title length shouldn't be min=3, max=120")
    private String title;
}