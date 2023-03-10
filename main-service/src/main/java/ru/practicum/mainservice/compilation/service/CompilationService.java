package ru.practicum.mainservice.compilation.service;

import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;

import java.util.List;

/**
 * Interface of service class for {@link Compilation} entity.
 *
 * @author DmitrySheyko
 */
public interface CompilationService {

    CompilationDto add(NewCompilationDto newCompilationDto);

    String deleteCompilation(Long compilationId);

    String deleteEventFromCompilation(Long compilationId, Long eventId);

    String addEventToCompilation(Long compilationId, Long eventId);

    String changePinnedForCompilation(Long compilationId, boolean isPinned);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(Long compilationId);
}