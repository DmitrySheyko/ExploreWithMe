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
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository repository;

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Set<Event> eventSet;
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            eventSet = Collections.emptySet();
        } else
            eventSet = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, eventSet);
        compilation = repository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        log.info("New compilation id={}, title={} successfully add", compilation.getId(), compilation.getTitle());
        return compilationDto;
    }

    @Override
    public String deleteCompilation(Long compilationId) {
        repository.deleteById(compilationId);
        return String.format("Successfully deleted compilation id=%s", compilationId);
    }

    @Override
    public String deleteEventFromCompilation(Long compilationId, Long eventId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation id=%s not found", compilationId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s not found", eventId)));
        if (compilation.getEvents().remove(event)) {
            repository.save(compilation);
            log.info("Successfully edited compilation id={}, deleted event id={}", compilationId, eventId);
            return String.format("Successfully edited compilation id=%s, successfully deleted event id=%s",
                    compilationId, eventId);
        }
        throw new NotFoundException(String.format("Event id=%s didn't found in compilation id=%s", eventId,
                compilationId));
    }

    @Override
    public String addEventToCompilation(Long compilationId, Long eventId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation id=%s not found", compilationId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s not found", eventId)));
        if (compilation.getEvents().contains(event)) {
            throw new ValidationException(String.format("Event id=%s already add in compilation id=%s", eventId,
                    compilationId));
        } else {
            compilation.getEvents().add(event);
            repository.save(compilation);
            log.info("Successfully edited compilation id={}, add event id={}", compilationId, eventId);
            return String.format("Successfully edited compilation id=%s Successfully add event id=%s",
                    compilationId, eventId);
        }
    }

    @Override
    public String changePinnedForCompilation(Long compilationId, boolean isPinned) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation id=%s not found", compilationId)));
        if (compilation.getPinned() != isPinned) {
            compilation.setPinned(isPinned);
            repository.save(compilation);
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
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation id=%s not found", compilationId)));
        CompilationDto compilationDto = CompilationMapper.toDto(compilation);
        log.info("Compilations id={} successfully received", compilationId);
        return compilationDto;
    }
}