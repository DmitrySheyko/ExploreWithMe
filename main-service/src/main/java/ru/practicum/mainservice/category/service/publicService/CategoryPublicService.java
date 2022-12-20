package ru.practicum.mainservice.category.service.publicService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryPublicService {
    CategoryService service;
    CategoryMapper categoryMapper;

    public List<CategoryDto> getAll(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Category> categoriesPage = service.findAll(pageable);
        List<CategoryDto> categoriesList = categoriesPage.stream()
                .map(category -> categoryMapper.toDto(category))
                .collect(Collectors.toList());
        log.info("List of categories from={} size={} successfully received", from, size);
        return categoriesList;
    }

    public CategoryDto getById(Long categoryId) {
        service.checkIsObjectInStorage(categoryId);
        Category category = service.getById(categoryId);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        log.info("Category id={} successfully received", categoryId);
        return categoryDto;
    }
}
