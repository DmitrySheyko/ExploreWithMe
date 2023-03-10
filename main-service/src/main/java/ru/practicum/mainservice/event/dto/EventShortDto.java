package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.user.dto.UserShortDto;

/**
 * Class of short version dto for {@link Event} entity.
 * For return information about {@link Event}
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto implements Comparable<EventShortDto> {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;

    @Override
    public int compareTo(EventShortDto o) {
        return Integer.compare(getViews(), o.views);
    }
}