package ru.practicum.mainservice.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.location.model.Location;

@Getter
@Setter
@EqualsAndHashCode
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
    private Integer participantLimit; //TODO  - означает отсутсвие огрвничений
    private Boolean requestModeration;
    private String title;
}
