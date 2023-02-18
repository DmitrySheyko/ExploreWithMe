package ru.practicum.statservice.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class of {@link StatsRequest} entity.
 * Used for getting information about search request terms.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique;
}