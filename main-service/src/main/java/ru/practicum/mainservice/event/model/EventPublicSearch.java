package ru.practicum.mainservice.event.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Class of {@link EventPublicSearch} entity.
 * Used for get information about {@link Event} search requests from users with public role.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicSearch {
    private Set<Long> categories;
    private Boolean onlyAvailable;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String text;
}