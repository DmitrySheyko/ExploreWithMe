package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Class of {@link EventAdminSearch} entity.
 * For searching {@link Event} by requests from users with admin role
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearch {
    private Set<Long> users;
    private Set<State> states;
    private Set<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}