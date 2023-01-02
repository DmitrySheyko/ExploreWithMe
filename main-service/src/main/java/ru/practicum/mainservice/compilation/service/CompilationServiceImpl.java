package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.Repository.CompilationRepository;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.service.EventServiceImpl;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventServiceImpl eventServiceImpl;
    private final CompilationRepository repository;

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            eventServiceImpl.checkIsObjectInStorage(newCompilationDto.getEvents());
        }
        Set<Event> eventSet;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            eventSet = Collections.emptySet();
        } else
            eventSet = newCompilationDto.getEvents().stream()
                    .map(eventServiceImpl::findById)
                    .collect(Collectors.toSet());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, eventSet);
        compilation = repository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        log.warn("New compilation id={}, title={} successfully add", compilation.getId(), compilation.getTitle());
        return compilationDto;
    }

    @Override
    @Transactional
    public String deleteCompilation(Long compilationId) {
        repository.deleteById(compilationId);
        return String.format("Successfully deleted compilation id=%s", compilationId);
    }

    @Override
    @Transactional
    public String deleteEventFromCompilation(Long compilationId, Long eventId) {
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
    @Transactional
    public String addEventToCompilation(Long compilationId, Long eventId) {
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
    @Transactional
    public String changePinnedForCompilation(Long compilationId, boolean isPinned) {
        Compilation compilation = findById(compilationId);
        if (compilation.getPinned() != isPinned) {
            compilation.setPinned(isPinned);
            update(compilation);
            log.info("Successfully updated compilation id={}, pinned={} ", compilationId, isPinned);
            return String.format("Successfully unpinned compilation id=%s, pinned=%s", compilationId, isPinned);
        }
        throw new ValidationException(String.format("Compilation id=%s pinned already changed", compilationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Compilation> compilationPage;
        if (pinned == null) {
            compilationPage = repository.findAll(pageable);
        } else {
            compilationPage = repository.findAllByPinned(pinned, pageable);
        }
        List<CompilationDto> compilationList = compilationPage.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of compilations pinned={} successfully received", pinned);
        return compilationList;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getById(Long compilationId) {
        Compilation compilation = findById(compilationId);
        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        log.info("Compilations id={} successfully received", compilationId);
        return compilationDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation findById(Long compilationId) {
        Optional<Compilation> optionalCompilation = repository.findById(compilationId);
        if (optionalCompilation.isPresent()) {
            return optionalCompilation.get();
        }
        log.warn("Information about compilation id={} is empty", compilationId);
        throw new NotFoundException((String.format("Compilation with id=%s was not found.", compilationId)));
    }

    private void update(Compilation compilation) {
        Compilation oldCompilation = findById(compilation.getId());
        if (compilation.getEvents() != null) {
            oldCompilation.setEvents(compilation.getEvents());
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        repository.save(oldCompilation);
    }
}