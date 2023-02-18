package ru.practicum.mainservice.compilation.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.compilation.model.Compilation;

/**
 * Interface of JpaRepository {@link Compilation} entity.
 *
 * @author DmitrySheyko
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}