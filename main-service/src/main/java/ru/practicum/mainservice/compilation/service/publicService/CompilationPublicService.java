package ru.practicum.mainservice.compilation.service.publicService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.Repository.CompilationRepository;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CompilationPublicService {
    private final CompilationService service;
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Compilation> compilationPage = service.findAll(pinned, pageable);
        List<CompilationDto> compilationList = compilationPage.stream()
                .map(compilation -> mapper.toDto(compilation))
                .collect(Collectors.toList());
        log.info("List of compilations pinned={} successfully received", pinned);
        return compilationList;
    }

    public CompilationDto getById(Long compilationId) {
        service.checkIsObjectInStorage(compilationId);
        Compilation compilation = service.findById(compilationId);
        CompilationDto compilationDto = mapper.toDto(compilation);
        log.info("Compilations id={} successfully received", compilationId);
        return compilationDto;
    }
}
