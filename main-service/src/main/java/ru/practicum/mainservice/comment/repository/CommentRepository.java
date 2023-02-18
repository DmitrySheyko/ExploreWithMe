package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.request.model.Status;

import java.util.Optional;

/**
 * Interface of JpaRepository for {@link Comment} entity.
 *
 * @author DmitrySheyko
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {
    Optional<Comment> findByUserIdAndEventId(Long userId, Long eventId);

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);

    Page<Comment> findAllByUserId(Long userId, Pageable pageable);

    Page<Comment> findAllByUserIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<Comment> findAllByEventIdAndStatusOrderByCreatedDesc(Long eventId, Status status, Pageable pageable);
}