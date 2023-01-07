package ru.practicum.mainservice.event.dto;

import lombok.*;

import java.util.Set;

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