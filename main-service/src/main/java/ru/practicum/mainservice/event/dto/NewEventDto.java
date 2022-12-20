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
public class NewEventDto {

    @Length(min = 20, max = 2000)
    private String annotation;
    private Category category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    private String title;
}
