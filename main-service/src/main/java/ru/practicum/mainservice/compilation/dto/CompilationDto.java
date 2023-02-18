package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.util.Set;

/**
 * Class of dto for {@link Compilation} entity.
 * Used for return information about {@link Compilation}.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}