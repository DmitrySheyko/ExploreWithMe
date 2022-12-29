package ru.practicum.mainservice.category.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.service.CategoryServiceImpl;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryServiceImpl.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryServiceImpl.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    public Map<String, Long> delete(@PathVariable("id") Long categoryId) {
        return categoryServiceImpl.delete(categoryId);
    }
}