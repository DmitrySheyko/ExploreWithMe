package ru.practicum.statservice.model;

import lombok.*;

/**
 * Class of {@link StatsResponse} entity.
 * Used for returning statistic information from stat-server.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
public class StatsResponse {
    public StatsResponse(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    private String app;
    private String uri;
    private Long hits;
}
