package ru.practicum.mainservice.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.location.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Class of dto for {@link Event} entity.
 * Used for creating new {@link Event} entity.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Event annotation shouldn't be blank")
    @Length(min = 20, max = 2000, message = "Event annotation length min=20, max= 1000")
    private String annotation;

    @NotNull(message = "Event category id shouldn't be null")
    @Positive(message = "Event category id should be positive")
    private Long category;

    @NotBlank(message = "Event description shouldn't be blank")
    private String description;

    @NotBlank(message = "EventDate shouldn't be blank")
    private String eventDate;

    @NotNull(message = "Event location shouldn't be null")
    private Location location;

    private Boolean paid;

    @PositiveOrZero(message = "Event participantLimit should be positive or zero")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Event title shouldn't be blank")
    @Length(min = 3, max = 120, message = "Event title length shouldn't be min=3, max=120")
    private String title;
}