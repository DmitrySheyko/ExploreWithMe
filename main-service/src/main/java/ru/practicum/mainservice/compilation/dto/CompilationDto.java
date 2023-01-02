package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}