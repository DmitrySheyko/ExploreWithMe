package ru.practicum.mainservice.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.Repository.CompilationRepository;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.CompilationUpdateDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    public Compilation save(Compilation compilation) {
        return repository.save(compilation);
    }

    public Map<String, Long> deleteById(Long compilationId) {
        repository.deleteById(compilationId);
        return Map.of("Successfully deleted compilation id=", compilationId);
    }

    public Compilation update(CompilationUpdateDto updateDto) {
        Compilation compilation = findById(updateDto.getId());
        if (updateDto.getEvents()!=null){
            compilation.setEvents(updateDto.getEvents());
            compilation = repository.save(compilation);
        }
        if (updateDto.getPinned()!=null){
            compilation.setPinned(updateDto.getPinned());
            compilation = repository.save(compilation);
        }
        return compilation;
    }

//    public Map<String, Long> deleteEventFromCompilation(Long compilationId, Long eventId){
//        Compilation compilation = findById(compilationId);
//        List<Event> eventsList = compilation.getEvents();
//        Event event = eventService.findById(eventId);
//        if(eventsList.remove(event)){
//            compilation.setEvents(eventsList);
//            return Map.of("Successfully edited compilation id=", compilationId,
//                    " Successfully deleted event id=", eventId);
//        }
//        throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", compilationId,
//                eventId), "The required object was not found.");
//    }

    public Page<Compilation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Compilation findById(Long compilationId) {
        Optional<Compilation> optionalCompilation = repository.findById(compilationId);
        if (optionalCompilation.isPresent()) {
            return optionalCompilation.get();
        }
        log.warn("Information about compilation id={} is empty", compilationId);
        throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilationId)),
                "The required object was not found.");
    }

    public Page<Compilation> findAll(Boolean pinned, Pageable pageable) {
         return repository.findAllByPinned(pinned, pageable);
    }

    public void checkIsObjectInStorage(Compilation compilation) {
        if (!repository.existsById(compilation.getId())) {
            log.warn("Compilation id={} not found", compilation.getId());
            throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilation.getId())),
                    "The required object was not found.");
        }
    }

    public void checkIsObjectInStorage(Long compilationId) {
        if (!repository.existsById(compilationId)) {
            log.warn("Compilation id={} not found", compilationId);
            throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilationId)),
                    "The required object was not found.");
        }
    }

    public void checkIsEventInCompilation(Long compilationId, Long eventId) {
        Compilation compilation = findById(compilationId);
        List<Long> eventsIdList = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        if (!eventsIdList.contains(eventId)) {
            throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", compilationId,
                    eventId), "The required object was not found.");
        }
    }
}
