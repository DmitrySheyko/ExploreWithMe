package ru.practicum.mainservice.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.service.EventService;

import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    EventService eventService;
    EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .events(compilationDto.getEvents().stream()
                        .map(id->eventService.findById(id))
                        .collect(Collectors.toList()))
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public CompilationDto toDto (Compilation compilation){
        return CompilationDto.builder()
                .events(compilation.getEvents().stream()
                        .map(event -> eventMapper.toShortDto(event))
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
