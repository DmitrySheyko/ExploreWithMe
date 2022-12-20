package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.State;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicSearchDto {
    private String text;
    private Long[] categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
}
