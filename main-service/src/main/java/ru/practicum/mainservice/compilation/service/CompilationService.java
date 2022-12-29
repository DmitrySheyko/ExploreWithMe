package ru.practicum.mainservice.compilation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    CompilationDto add(NewCompilationDto newCompilationDto);

    String deleteCompilation(Long compilationId);

    String deleteEventFromCompilation(Long compilationId, Long eventId);

    String addEventToCompilation(Long compilationId, Long eventId);

    String unPinCompilation(Long compilationId);

    String pinCompilation(Long compilationId);

    void update(Compilation compilation);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(Long compilationId);

    Compilation findById(Long compilationId);

    Page<Compilation> findAll(Boolean pinned, Pageable pageable);

    void checkIsObjectInStorage(Long compilationId);

    void checkIsEventInCompilation(Long compilationId, Long eventId);

    void checkIsEventsExist(List<Long> compilationsList);
}