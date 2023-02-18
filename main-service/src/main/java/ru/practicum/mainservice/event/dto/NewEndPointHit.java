package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

/**
 * Class of {@link NewEndPointHit} entity.
 * Used for sending information to stat-server about new {@link Event} search requests.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEndPointHit {
    private final String app = "ewm-main-service";
    private String uri;
    private String ip;
    private String timeStamp;
}