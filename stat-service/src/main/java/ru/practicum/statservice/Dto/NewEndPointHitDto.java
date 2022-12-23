package ru.practicum.statservice.Dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEndPointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timeStamp;
}
