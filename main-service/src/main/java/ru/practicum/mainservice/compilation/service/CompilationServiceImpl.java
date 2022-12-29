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
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventServiceImpl;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventServiceImpl eventServiceImpl;
    private final CompilationMapper mapper;
    private final CompilationRepository repository;

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            checkIsEventsExist(newCompilationDto.getEvents());
        }
        Compilation compilation = mapper.toCompilation(newCompilationDto);
        compilation = repository.save(compilation);
        CompilationDto compilationDto = mapper.toDto(compilation);
        log.warn("New compilation id={}, title={} successfully add", compilation.getId(), compilation.getTitle());
        return compilationDto;
    }

    @Override
    public String deleteCompilation(Long compilationId) {
        repository.deleteById(compilationId);
        return String.format("Successfully deleted compilation id=%s", compilationId);
    }

    @Override
    public String deleteEventFromCompilation(Long compilationId, Long eventId) {
        checkIsObjectInStorage(compilationId);
        checkIsEventInCompilation(compilationId, eventId);
        Compilation compilation = findById(compilationId);
        Event event = eventServiceImpl.findById(eventId);
        if (compilation.getEvents().remove(event)) {
            update(compilation);
            log.info("Successfully edited compilation id={}, deleted event id={}", compilationId, eventId);
            return String.format("Successfully edited compilation id=%s, successfully deleted event id=%s",
                    compilationId, eventId);
        }
        throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", eventId,
                compilationId));
    }

    @Override
    public String addEventToCompilation(Long compilationId, Long eventId) {
        checkIsObjectInStorage(compilationId);
        eventServiceImpl.checkIsObjectInStorage(eventId);
        Compilation compilation = findById(compilationId);
        Event event = eventServiceImpl.findById(eventId);
        if (compilation.getEvents().contains(event)) {
            throw new ValidationException(String.format("Event id=%s already add in compilation id=%s", eventId,
                    compilationId));
        } else {
            compilation.getEvents().add(event);
            update(compilation);
            log.info("Successfully edited compilation id={}, add event id={}", compilationId, eventId);
            return String.format("Successfully edited compilation id=%s Successfully add event id=%s",
                    compilationId, eventId);
        }
    }

    @Override
    public String unPinCompilation(Long compilationId) {
        checkIsObjectInStorage(compilationId);
        Compilation compilation = findById(compilationId);
        if (compilation.getPinned()) {
            compilation.setPinned(false);
            update(compilation);
            log.info("Successfully unpinned compilation id={}, ", compilationId);
            return String.format("Successfully unpinned compilation id=%s", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s unpinned already", compilationId));
    }

    @Override
    public String pinCompilation(Long compilationId) {
        checkIsObjectInStorage(compilationId);
        Compilation compilation = findById(compilationId);
        if (!compilation.getPinned()) {
            compilation.setPinned(true);
            update(compilation);
            log.info("Successfully pinned compilation id={}, ", compilationId);
            return String.format("Successfully pinned compilation id=%s", compilationId);
        }
        throw new ValidationException(String.format("Compilation id=%s pinned already", compilationId));
    }

    @Override
    public void update(Compilation compilation) {
        Compilation oldCompilation = findById(compilation.getId());
        if (compilation.getEvents() != null) {
            oldCompilation.setEvents(compilation.getEvents());
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        repository.save(oldCompilation);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Compilation> compilationPage = findAll(pinned, pageable);
        List<CompilationDto> compilationList = compilationPage.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.info("List of compilations pinned={} successfully received", pinned);
        return compilationList;
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        checkIsObjectInStorage(compilationId);
        Compilation compilation = findById(compilationId);
        CompilationDto compilationDto = mapper.toDto(compilation);
        log.info("Compilations id={} successfully received", compilationId);
        return compilationDto;
    }

    @Override
    public Compilation findById(Long compilationId) {
        Optional<Compilation> optionalCompilation = repository.findById(compilationId);
        if (optionalCompilation.isPresent()) {
            return optionalCompilation.get();
        }
        log.warn("Information about compilation id={} is empty", compilationId);
        throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilationId)));
    }

    @Override
    public Page<Compilation> findAll(Boolean pinned, Pageable pageable) {
        return repository.findAllByPinned(pinned, pageable);
    }

    @Override
    public void checkIsObjectInStorage(Long compilationId) {
        if (!repository.existsById(compilationId)) {
            log.warn("Compilation id={} not found", compilationId);
            throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilationId)));
        }
    }

    @Override
    public void checkIsEventInCompilation(Long compilationId, Long eventId) {
        Compilation compilation = findById(compilationId);
        List<Long> eventsIdList = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        if (!eventsIdList.contains(eventId)) {
            throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s",
                    eventId, compilationId));
        }
    }

    @Override
    public void checkIsEventsExist(List<Long> compilationsList) {
        for (Long id : compilationsList) {
            eventServiceImpl.checkIsObjectInStorage(id);
        }
    }
}