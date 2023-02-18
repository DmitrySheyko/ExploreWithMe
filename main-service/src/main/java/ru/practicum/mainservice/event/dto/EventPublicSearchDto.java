package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

import java.util.Set;

/**
 * Class of dto for {@link Event} entity.
 * For {@link Event} searching requests by users with public role
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicSearchDto {
    private String text;
    private Set<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
}