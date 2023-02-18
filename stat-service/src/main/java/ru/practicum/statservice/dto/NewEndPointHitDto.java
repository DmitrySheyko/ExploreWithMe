package ru.practicum.statservice.dto;

import lombok.*;
import ru.practicum.statservice.model.EndPointHit;

/**
 * Class of dto for {@link EndPointHit} entity.
 * Used for getting information for creation new {@link EndPointHit}.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEndPointHitDto {
    private Long id;
    private final String app = "ewm-main-service";
    private String uri;
    private String ip;
    private String timeStamp;
}
