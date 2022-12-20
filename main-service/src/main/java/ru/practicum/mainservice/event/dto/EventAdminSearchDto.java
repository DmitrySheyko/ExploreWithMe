package ru.practicum.mainservice.event.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearchDto {
    private Long[] users;
    private String[] states;
    private Long[] categories;
    private String rangeStart;
    private String rangeEnd;
}
