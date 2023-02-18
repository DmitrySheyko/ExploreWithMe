package ru.practicum.mainservice.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for {@link Compilation} entity.
 *
 * @author DmitrySheyko
 */
@Component
@RequiredArgsConstructor
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto compilationDto, Set<Event> eventSet) {
        return Compilation.builder()
                .events(eventSet)
                .pinned(compilationDto.isPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toShortDto)
                        .collect(Collectors.toSet()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}