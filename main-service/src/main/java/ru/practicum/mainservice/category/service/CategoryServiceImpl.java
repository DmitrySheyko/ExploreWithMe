package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for {@link Category} entity.
 * Implements interface {@link CategoryService}
 *
 * @author DmitrySheyko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toEntity(newCategoryDto);
        category = repository.save(category);
        CategoryDto categoryDto = CategoryMapper.toDto(category);
        log.info("New category name={} id={} successfully created", category.getName(), category.getId());
        return categoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Long categoryId = categoryDto.getId();
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", categoryId)));
        category.setName(categoryDto.getName());
        category = repository.save(category);
        categoryDto = CategoryMapper.toDto(category);
        log.info("Category id={} new name={} successfully updated", category.getId(), category.getName());
        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Category> categoriesPage = repository.findAll(pageable);
        List<CategoryDto> categoriesList = categoriesPage.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of categories from={} size={} successfully received", from, size);
        return categoriesList;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", categoryId)));
        CategoryDto categoryDto = CategoryMapper.toDto(category);
        log.info("Category id={} successfully received", categoryId);
        return categoryDto;
    }

    @Override
    public String delete(Long categoryId) {
        repository.deleteById(categoryId);
        return String.format("Successfully deleted category id=%s", categoryId);
    }
}