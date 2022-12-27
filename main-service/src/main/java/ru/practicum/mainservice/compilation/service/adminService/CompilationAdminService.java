package ru.practicum.mainservice.compilation.service.adminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.CompilationUpdateDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CompilationAdminService {
    private final EventService eventService;
    private final CompilationService service;
    private final CompilationMapper mapper;

    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if(newCompilationDto.getEvents() != null && ! newCompilationDto.getEvents().isEmpty()) {
            checkIsEventsExist(newCompilationDto.getEvents());
        }
        Compilation compilation = mapper.toCompilation(newCompilationDto);
        compilation = service.save(compilation);
        CompilationDto compilationDto = mapper.toDto(compilation);
        log.warn("New compilation id={}, title={} successfully add", compilation.getId(), compilation.getTitle());
        return compilationDto;
    }

    public Map<String, Long> deleteCompilation(Long compId) {
        return service.deleteById(compId);
    }

    public Map<String, Long> deleteEventFromCompilation(Long compilationId, Long eventId) {
        service.checkIsObjectInStorage(compilationId);
        service.checkIsEventInCompilation(compilationId, eventId);
        Compilation compilation = service.findById(compilationId);
        Event event = eventService.findById(eventId);
        if (compilation.getEvents().remove(event)) {
            service.update(compilation);
            log.info("Successfully edited compilation id={}, deleted event id={}", compilationId, eventId);
            return Map.of("Successfully edited compilation id=", compilationId,
                    " Successfully deleted event id=", eventId);
        }
        throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", eventId,
                compilationId));
    }

    public Map<String, Long> addEventToCompilation(Long compilationId, Long eventId) {
        service.checkIsObjectInStorage(compilationId);
        eventService.checkIsObjectInStorage(eventId);
        Compilation compilation = service.findById(compilationId);
        Event event = eventService.findById(eventId);
        if (compilation.getEvents().contains(event)) {
            throw new ValidationException(String.format("Event id=%s already add in compilation id=%s", eventId,
                    compilationId));
        } else {
            compilation.getEvents().add(event);
            service.update(compilation);
            log.info("Successfully edited compilation id={}, add event id={}", compilationId, eventId);
            return Map.of("Successfully edited compilation id=", compilationId,
                    " Successfully add event id=", eventId);
        }
    }


    public Map<String, Long> unPinCompilation(Long compilationId) {
        service.checkIsObjectInStorage(compilationId);
        Compilation compilation = service.findById(compilationId);
        if (compilation.getPinned()) {
            compilation.setPinned(false);
            service.update(compilation);
            log.info("Successfully unpinned compilation id={}, ", compilationId);
            return Map.of("Successfully unpinned compilation id=", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s unpinned already", compilationId));
    }

    public Map<String, Long> pinCompilation(Long compilationId) {
        service.checkIsObjectInStorage(compilationId);
        Compilation compilation = service.findById(compilationId);
        if (!compilation.getPinned()) {
            compilation.setPinned(true);
            service.update(compilation);
            log.info("Successfully pinned compilation id={}, ", compilationId);
            return Map.of("Successfully pinned compilation id=", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s pinned already", compilationId));
    }

    public void checkIsEventsExist(List<Long> compilationsList) {
        for (Long id : compilationsList) {
            eventService.checkIsObjectInStorage(id);
        }
    }
}