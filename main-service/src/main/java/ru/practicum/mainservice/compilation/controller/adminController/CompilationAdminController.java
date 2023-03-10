package ru.practicum.mainservice.compilation.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;

/**
 * Class of controller for {@link Compilation} entity for users with Admin role
 *
 * @author DmitrySheyko
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/compilations")
public class CompilationAdminController {
    private final CompilationService service;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return service.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public String deleteCompilation(@PathVariable("compId") Long compilationId) {
        return service.deleteCompilation(compilationId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public String deleteEventFromCompilation(@PathVariable("compId") Long compilationId,
                                             @PathVariable("eventId") Long eventId) {
        return service.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public String addEventToCompilation(@PathVariable("compId") Long compilationId,
                                        @PathVariable("eventId") Long eventId) {
        return service.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("{compId}/pin")
    public String unPinCompilation(@PathVariable("compId") Long compilationId) {
        return service.changePinnedForCompilation(compilationId, false);
    }

    @PatchMapping("{compId}/pin")
    public String pinCompilation(@PathVariable("compId") Long compilationId) {
        return service.changePinnedForCompilation(compilationId, true);
    }
}