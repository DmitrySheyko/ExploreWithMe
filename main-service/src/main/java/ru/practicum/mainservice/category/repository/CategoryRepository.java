package ru.practicum.mainservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.category.model.Category;

/**
 * Interface of JpaRepository for {@link Category} entity.
 *
 * @author DmitrySheyko
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}