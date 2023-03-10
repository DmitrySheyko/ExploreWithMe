package ru.practicum.mainservice.compilation.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Class of controller for {@link Compilation} entity for users with Public role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable("compId") Long compId) {
        return service.getById(compId);
    }
}