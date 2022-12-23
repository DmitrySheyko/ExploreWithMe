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
        checkIsEventsExist(newCompilationDto.getEvents());
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
        List<Event> eventsList = service.findById(compilationId).getEvents();
        Event event = eventService.findById(eventId);
        if (eventsList.remove(event)) {
            service.update(CompilationUpdateDto.builder()
                    .events(eventsList)
                    .build());
            log.info("Successfully edited compilation id={}, deleted event id={}", compilationId, eventId);
            return Map.of("Successfully edited compilation id=", compilationId,
                    " Successfully deleted event id=", eventId);
        }
        throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", eventId,
                compilationId), "The required object was not found.");
    }

    public Map<String, Long> addEventToCompilation(Long compilationId, Long eventId) {
        service.checkIsObjectInStorage(compilationId);
        List<Event> eventsList = service.findById(compilationId).getEvents();
        Event event = eventService.findById(eventId);
        if (eventsList.contains(event)) {
            throw new NotFoundException(String.format("Event id=%s already exist in compilation id=%s", eventId,
                    compilationId), "The required object was not found.");
        } else {
            eventsList.add(event);
            service.update(CompilationUpdateDto.builder()
                    .events(eventsList)
                    .build());
            log.info("Successfully edited compilation id={}, add event id={}", compilationId, eventId);
            return Map.of("Successfully edited compilation id=", compilationId,
                    " Successfully add event id=", eventId);
        }
    }


    public Map<String, Long> unPinCompilation(Long compilationId, Long eventId) {
        service.checkIsObjectInStorage(compilationId);
        if (service.findById(compilationId).getPinned()) {
            service.update(CompilationUpdateDto.builder()
                    .pinned(false)
                    .build());
            log.info("Successfully unpinned compilation id={}, ", compilationId);
            return Map.of("Successfully unpinned compilation id=", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s unpinned already", compilationId),
                "For the requested operation the conditions are not met.");
    }

    public Map<String, Long> pinCompilation(Long compilationId, Long eventId) {
        service.checkIsObjectInStorage(compilationId);
        if (!service.findById(compilationId).getPinned()) {
            service.update(CompilationUpdateDto.builder()
                    .pinned(true)
                    .build());
            log.info("Successfully pinned compilation id={}, ", compilationId);
            return Map.of("Successfully pinned compilation id=", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s pinned already", compilationId),
                "For the requested operation the conditions are not met.");
    }

    public void checkIsEventsExist(List<Long> compilationsList) {
        for (Long id : compilationsList) {
            eventService.checkIsObjectInStorage(id);
        }
    }
}