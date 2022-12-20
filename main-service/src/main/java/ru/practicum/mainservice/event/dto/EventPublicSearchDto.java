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
    String text;
    Integer[] categories;
    Boolean paid;
    String rangeStart;
    String rangeEnd;
    Boolean onlyAvailable;


//    public
//    Integer[] users;
//    String[] states;
//    String[] categories;
//    String rangeStart;
//    String rangeEnd;
}
