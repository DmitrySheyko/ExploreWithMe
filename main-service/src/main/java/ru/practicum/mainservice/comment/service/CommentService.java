package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.model.Status;

import java.util.List;

/**
 * Interface of service class for {@link Event} entity.
 *
 * @author DmitrySheyko
 */
public interface CommentService {

    CommentDto add(Long userId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, UpdateCommentDto commentDto);

    List<CommentDto> getAllByUserIdAndStatus(Long userId, Status status, int from, int size);

    String delete(Long userId, Long commentId);

    CommentDto publish(Long commentId);

    CommentDto reject(Long commentId);

    List<CommentDto> getAllPublishedByEventId(Long eventId, int from, int size);

    List<CommentDto> search(CommentAdminSearchDto searchDto, int from, int size, String sort);
}