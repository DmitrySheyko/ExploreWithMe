package ru.practicum.mainservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

/**
 * Interface of JpaRepository for {@link User} entity
 *
 * @author DmitrySheyko
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}