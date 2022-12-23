package ru.practicum.statservice.Dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatRequestDto {
    String start;
    String end;
    String[] uris;
    Boolean unique;
}
