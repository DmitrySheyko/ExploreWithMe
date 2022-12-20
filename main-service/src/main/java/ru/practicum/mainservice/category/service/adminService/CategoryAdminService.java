package ru.practicum.mainservice.category.service.adminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryAdminService {
    CategoryService service;
    CategoryMapper categoryMapper;

    public CategoryDto add(NewCategoryDto newCategoryDto) {
        // проверка на уникальность имени категории
        Category category = categoryMapper.toEntity(newCategoryDto);
        category = service.save(category);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        log.info("New category name={} id={} successfully created", category.getName(), category.getId());
        return categoryDto;
    }

    public CategoryDto update(CategoryDto categoryDto) {
        service.checkIsObjectInStorage(categoryDto.getId());
        Category category = categoryMapper.toEntity(categoryDto);
        category = service.save(category);
        categoryDto = categoryMapper.toDto(category);
        log.info("Category id={} new name={} successfully updated", category.getId(), category.getName());
        return categoryDto;
    }

    public Map<String, Long> delete(Long categoryId) {
        service.checkIsObjectInStorage(categoryId);
        Map<String, Long> response = service.deleteById(categoryId);
        log.info("Category id={} successfully deleted", categoryId);
        return response;
    }
}
