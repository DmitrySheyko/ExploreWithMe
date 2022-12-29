package ru.practicum.mainservice.compilation.controller.publicController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {
    private final CompilationServiceImpl service;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(value = "pinned", required = false, defaultValue = "true") Boolean pinned,
                                       @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) int from,
                                       @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) int size) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable("compId") Long compId) {
        return service.getById(compId);
    }
}