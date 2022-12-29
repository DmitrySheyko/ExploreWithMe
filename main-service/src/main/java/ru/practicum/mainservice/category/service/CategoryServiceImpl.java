package ru.practicum.mainservice.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryInterface {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toEntity(newCategoryDto);
        category = repository.save(category);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        log.info("New category name={} id={} successfully created", category.getName(), category.getId());
        return categoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        checkIsObjectInStorage(categoryDto.getId());
        Category category = categoryMapper.toEntity(categoryDto);
        category = repository.save(category);
        categoryDto = categoryMapper.toDto(category);
        log.info("Category id={} new name={} successfully updated", category.getId(), category.getName());
        return categoryDto;
    }

    @Override
    public Map<String, Long> delete(Long categoryId) {
        checkIsObjectInStorage(categoryId);
        Map<String, Long> response = deleteById(categoryId);
        log.info("Category id={} successfully deleted", categoryId);
        return response;
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Category> categoriesPage = repository.findAll(pageable);
        List<CategoryDto> categoriesList = categoriesPage.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        log.info("List of categories from={} size={} successfully received", from, size);
        return categoriesList;
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        checkIsObjectInStorage(categoryId);
        Category category = findById(categoryId);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        log.info("Category id={} successfully received", categoryId);
        return categoryDto;
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Category findById(Long categoryId) {
        Optional<Category> optionalCategory = repository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        throw new NotFoundException((String.format("Category with id=%s was not found.", categoryId)));
    }

    @Override
    public Map<String, Long> deleteById(Long categoryId) {
        repository.deleteById(categoryId);
        return Map.of("Successfully deleted category id=", categoryId);
    }

    @Override
    public void checkIsObjectInStorage(Long categoryId) {
        if (!repository.existsById(categoryId)) {
            log.warn("Category with id={}} was not found.", categoryId);
            throw new NotFoundException((String.format("Category with id=%s was not found.", categoryId)));
        }
    }
}