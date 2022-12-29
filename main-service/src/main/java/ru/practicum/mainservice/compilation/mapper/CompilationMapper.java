package ru.practicum.mainservice.compilation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapper {
    private final EventServiceImpl eventServiceImpl;
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .events(intToEvent(compilationDto.getEvents()))
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    private List<Event> intToEvent(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        return idList.stream().map(eventServiceImpl::findById).collect(Collectors.toList());
    }
}