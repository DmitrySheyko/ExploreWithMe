package ru.practicum.statservice.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponce {
    private String app;
    private String uri;
    Integer hits;
}
