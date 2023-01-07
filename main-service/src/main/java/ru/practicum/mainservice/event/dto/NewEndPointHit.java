package ru.practicum.mainservice.event.dto;

import lombok.*;

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