package ru.practicum.mainservice.category.service;

import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.model.Category;

import java.util.List;

/**
 * Interface of service class for {@link Category} entity.
 *
 * @author DmitrySheyko
 */
public interface CategoryService {

    CategoryDto add(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    String delete(Long categoryId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(Long categoryId);
}