package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.location.model.Location;

/**
 * Class of dto for {@link Event} entity.
 * For updating {@link Event} by requests from users with admin role
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Long eventId;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}