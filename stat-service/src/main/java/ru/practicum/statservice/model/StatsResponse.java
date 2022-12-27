package ru.practicum.statservice.model;

import lombok.*;

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

    @Override
    public String toString() {
        return "StatsResponse{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }
}
