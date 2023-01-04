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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryInterface {
    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toEntity(newCategoryDto);
        category = repository.save(category);
        CategoryDto categoryDto = CategoryMapper.toDto(category);
        log.info("New category name={} id={} successfully created", category.getName(), category.getId());
        return categoryDto;
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = findById(categoryDto.getId());
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
        Category category = findById(categoryId);
        CategoryDto categoryDto = CategoryMapper.toDto(category);
        log.info("Category id={} successfully received", categoryId);
        return categoryDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override //TODO заменить на getById
    @Transactional(readOnly = true)
    public Category findById(Long categoryId) {
        Optional<Category> optionalCategory = repository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        throw new NotFoundException((String.format("Category id=%s was not found.", categoryId)));
    }

    @Override
    @Transactional
    public String delete(Long categoryId) {
        repository.deleteById(categoryId);
        return String.format("Successfully deleted category id=%s", categoryId);
    }
}