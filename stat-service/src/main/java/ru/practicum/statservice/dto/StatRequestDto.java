package ru.practicum.statservice.dto;

import lombok.*;
import ru.practicum.statservice.model.StatsRequest;

import java.util.List;

/**
 * Class of dto for {@link StatsRequest} entity.
 * Used for getting information about search request terms.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatRequestDto {
    private String start;
    private String end;
    private List<String> uris;
    private Boolean unique;
}
