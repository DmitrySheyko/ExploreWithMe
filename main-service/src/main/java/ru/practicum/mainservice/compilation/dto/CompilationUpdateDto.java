package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.Event;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdateDto {
    List<Event> events;
    Long id;
    Boolean pinned;
}
