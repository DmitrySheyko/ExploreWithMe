package ru.practicum.mainservice.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService {
    CategoryRepository repository;

    public Category save(Category category) {
        return repository.save(category);
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Category getById(Long categoryId) {
        Optional<Category> optionalCategory = repository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        throw new NotFoundException((String.format("Category with id=%s was not found.", categoryId)),
                "The required object was not found.");
    }

    public Map<String, Long> deleteById(Long categoryId) {
        repository.deleteById(categoryId);
        return Map.of("Successfully deleted category id=", categoryId);
    }

    public void checkIsObjectInStorage(Long categoryId) {
        if (!repository.existsById(categoryId)) {
            log.warn("Category with id={}} was not found.", categoryId);
            throw new NotFoundException((String.format("Category with id=%s was not found.", categoryId)),
                    "The required object was not found.");
        }
    }
}
