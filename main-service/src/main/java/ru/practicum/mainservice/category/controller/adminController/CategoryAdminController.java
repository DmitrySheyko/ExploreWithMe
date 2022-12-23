package ru.practicum.mainservice.category.controller.adminController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.service.adminService.CategoryAdminService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    CategoryAdminService categoryAdminService;

    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryAdminService.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    public Map<String, Long> delete(@PathVariable("id") Long categoryId) {
        return categoryAdminService.delete(categoryId);
    }

}
