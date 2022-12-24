package ru.practicum.mainservice.compilation.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.service.adminService.CompilationAdminService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("admin/compilations")
public class CompilationAdminController {
    private final CompilationAdminService service;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return service.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    Map<String, Long> deleteCompilation(@PathVariable("compId") Long compilationId) {
        return service.deleteCompilation(compilationId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    Map<String, Long> deleteEventFromCompilation(@PathVariable("compId") Long compilationId,
                                                 @PathVariable("eventId") Long eventId) {
        return service.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    Map<String, Long> addEventToCompilation(@PathVariable("compId") Long compilationId,
                                            @PathVariable("eventId") Long eventId) {
        return service.addEventToCompilation(compilationId, eventId);
    }

    @DeleteMapping("{compId}/pin")
    Map<String, Long> unPinCompilation (@PathVariable("compId") Long compilationId,
                                                 @PathVariable("eventId") Long eventId) {
        return service.unPinCompilation(compilationId, eventId);
    }

    @PatchMapping("{compId}/pin")
    Map<String, Long> pinCompilation(@PathVariable("compId") Long compilationId,
                                            @PathVariable("eventId") Long eventId) {
        return service.pinCompilation(compilationId, eventId);
    }
}