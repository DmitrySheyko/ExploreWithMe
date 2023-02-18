package ru.practicum.mainservice.event.dto;

import lombok.*;

/**
 * Class of dto for getting statistic information from stats-server.
 *
 * @author DmitrySheyko
 */
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