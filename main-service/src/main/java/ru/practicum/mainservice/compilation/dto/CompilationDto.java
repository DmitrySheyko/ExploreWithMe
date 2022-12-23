package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}
