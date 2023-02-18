package ru.practicum.mainservice.category.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

/**
 * Class of controller for {@link Category} entity for users with Admin role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService service;

    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return service.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        return service.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long categoryId) {
        return service.delete(categoryId);
    }
}