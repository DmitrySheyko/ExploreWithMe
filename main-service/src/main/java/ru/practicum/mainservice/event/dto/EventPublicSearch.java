package ru.practicum.mainservice.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicSearch {
    private List<Long> categories;
    private Boolean onlyAvailable;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String text;

    @Override
    public String toString() {
        return "EventPublicSearch{" +
                "categories=" + categories +
                ", onlyAvailable=" + onlyAvailable +
                ", paid=" + paid +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", text='" + text + '\'' +
                '}';
    }
}