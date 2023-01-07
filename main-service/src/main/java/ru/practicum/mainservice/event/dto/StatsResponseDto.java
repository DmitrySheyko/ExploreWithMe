package ru.practicum.mainservice.event.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponseDto {
    private String app;
    private String uri;
    private Integer hits;
}