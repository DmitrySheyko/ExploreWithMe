package ru.practicum.statservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequest {
    LocalDateTime start;
    LocalDateTime end;
    List<String> uris;
    Boolean unique;
}