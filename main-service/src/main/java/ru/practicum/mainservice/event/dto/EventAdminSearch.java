package ru.practicum.mainservice.event.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearch {
    private Long[] users;
    private Integer[] states;
    private Long[] categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
