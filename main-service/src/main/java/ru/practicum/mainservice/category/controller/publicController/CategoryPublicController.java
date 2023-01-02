package ru.practicum.mainservice.category.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.service.CategoryServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) int from,
                                    @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) int size) {
        return categoryServiceImpl.getAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable("id") Long categoryId) {
        return categoryServiceImpl.getById(categoryId);
    }
}