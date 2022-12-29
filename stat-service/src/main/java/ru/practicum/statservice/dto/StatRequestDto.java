package ru.practicum.statservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatRequestDto {
    String start;
    String end;
    List<String> uris;
    Boolean unique;
}
