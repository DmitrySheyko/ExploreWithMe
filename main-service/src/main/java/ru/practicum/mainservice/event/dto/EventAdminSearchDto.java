package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

import java.util.Set;

/**
 * Class of dto for {@link Event} entity.
 * For {@link Event} searching requests by users with admin role
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearchDto {
    private Set<Long> users;
    private Set<String> states;
    private Set<Long> categories;
    private String rangeStart;
    private String rangeEnd;
}