package ru.practicum.statservice.Dto;

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

    @Override
    public String toString() {
        return "StatRequestDto{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", uris=" + uris +
                ", unique=" + unique +
                '}';
    }
}
