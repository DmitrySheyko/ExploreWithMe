package ru.practicum.mainservice.comment.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.comment.dto.*;
import ru.practicum.mainservice.comment.model.QComment;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exceptions.NotFoundException;
import ru.practicum.mainservice.exceptions.ValidationException;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto add(Long userId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException((String.format("User id=%s was not found.", userId))));
        Event event = eventRepository.findById(newCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException((String.format("Event id=%s was not found.", newCommentDto.getEvent()))));
        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Comment didn't add. Initiator couldn't leave comments to own events");
        }
        if (repository.findByUserIdAndEventId(userId, newCommentDto.getEvent()).isPresent()) {
            throw new ValidationException((String.format("User id=%s already add comment to event id=%s.", userId,
                    newCommentDto.getEvent())));
        }
        LocalDateTime created = LocalDateTime.now();
        Comment comment = CommentMapper.toComment(user, event, created, newCommentDto.getText());
        comment.setStatus(Status.PENDING);
        comment = repository.save(comment);
        CommentDto commentDto = CommentMapper.toDto(comment);
        log.info("Comment id={} successfully add", comment.getId());
        return commentDto;
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, UpdateCommentDto commentDto) {
        Comment comment = repository.findByIdAndUserId(commentDto.getId(), userId)
                .orElseThrow(() -> new NotFoundException((String.format("Comment id=%s by user id=%s was not found.",
                        commentDto.getId(), userId))));
        comment.setText(Optional.ofNullable(commentDto.getText()).orElse(comment.getText()));
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(Status.PENDING);
        comment = repository.save(comment);
        CommentDto updatedCommentDto = CommentMapper.toDto(comment);
        log.info("Comment id={} successfully add", updatedCommentDto.getId());
        return updatedCommentDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByUserIdAndStatus(Long userId, Status status, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("created"));
        Page<Comment> commentsPage;
        if (status == null) {
            commentsPage = repository.findAllByUserId(userId, pageable);
        } else {
            commentsPage = repository.findAllByUserIdAndStatus(userId, status, pageable);
        }
        List<CommentDto> commentDtoSet = commentsPage.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        log.info("Comments set of user id={} successfully received", userId);
        return commentDtoSet;
    }

    @Override
    @Transactional
    public String delete(Long userId, Long commentId) {
        Comment comment = repository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundException((String.format("Comment id=%s by user id=%s was not found.",
                        commentId, userId))));
        repository.delete(comment);
        log.info("Comments id={} successfully deleted", commentId);
        return String.format("Comments id=%s successfully deleted", commentId);
    }

    @Override
    @Transactional
    public CommentDto publish(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException((String.format("Comment id=%s was not found.", commentId))));
        if (!comment.getStatus().equals(Status.PENDING)) {
            throw new ValidationException((String.format("Comment didn't published. For publish comment should has " +
                    "status Pending. Comment id=%s has status - %s", commentId, comment.getStatus())));
        }
        comment.setStatus(Status.CONFIRMED);
        comment = repository.save(comment);
        CommentDto updatedCommentDto = CommentMapper.toDto(comment);
        log.info("Comment id={} successfully published", updatedCommentDto.getId());
        return updatedCommentDto;
    }

    @Override
    @Transactional
    public CommentDto reject(Long commentId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException((String.format("Comment id=%s was not found.", commentId))));
        if (!comment.getStatus().equals(Status.PENDING)) {
            throw new ValidationException((String.format("Comment didn't rejected. For reject comment should has " +
                    "status Pending. Comment id=%s has status - %s", commentId, comment.getStatus())));
        }
        comment.setStatus(Status.REJECTED);
        comment = repository.save(comment);
        CommentDto updatedCommentDto = CommentMapper.toDto(comment);
        log.info("Comment id={} rejected", updatedCommentDto.getId());
        return updatedCommentDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllPublishedByEventId(Long eventId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(11);
        Page<Comment> commentsPage = repository.findAllByEventIdAndStatusOrderByCreatedDesc(eventId,
                Status.CONFIRMED, pageable);
        System.out.println(22);
        List<CommentDto> commentDtoSet = commentsPage.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        System.out.println(33);
        log.info("Published comments set for event id={} successfully received", eventId);
        return commentDtoSet;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> search(CommentAdminSearchDto searchDto, int from, int size, String sort) {
        CommentAdminSearch adminSearch = CommentMapper.toAdminSearch(searchDto);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = new BooleanBuilder();
        if (adminSearch.getUsers() != null && !adminSearch.getUsers().isEmpty()) {
            builder.and(QComment.comment.user.id.in(adminSearch.getUsers()));
        }
        if (adminSearch.getEvents() != null && !adminSearch.getEvents().isEmpty()) {
            builder.and(QComment.comment.event.id.in(adminSearch.getEvents()));
        }
        if (adminSearch.getText() != null && (!adminSearch.getText().isBlank())) {
            builder.and(QComment.comment.text.toUpperCase().contains(adminSearch.getText().toUpperCase()));
        }
        if (adminSearch.getRangeStart() != null) {
            builder.and(QComment.comment.created.after(adminSearch.getRangeStart()));
        }
        if (adminSearch.getRangeEnd() != null) {
            builder.and(QComment.comment.created.before(adminSearch.getRangeEnd()));
        }
        if (adminSearch.getStatus() != null && !adminSearch.getStatus().isEmpty()) {
            builder.and(QComment.comment.status.in(adminSearch.getStatus()));
        }
        Page<Comment> commentsPage = repository.findAll(builder, pageable);
        List<CommentDto> commentDtoList = commentsPage.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        log.info("Searched list of comments successfully receives");
        return commentDtoList;
    }
}
