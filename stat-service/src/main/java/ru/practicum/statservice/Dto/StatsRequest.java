package ru.practicum.statservice.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequest {
    LocalDateTime start;
    LocalDateTime end;
    String[] uris;
    Boolean unique;
}
