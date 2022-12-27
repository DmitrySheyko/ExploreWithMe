package ru.practicum.statservice.Dto;

import lombok.*;

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

    @Override
    public String toString() {
        return "NewEndPointHitDto{" +
                ", app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
