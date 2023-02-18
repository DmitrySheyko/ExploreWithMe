package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Class of controller for {@link Comment} entity for users with user role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService service;

    // Создать comment. После создания comment отправляется на модерацию.
    @PostMapping
    public CommentDto add(@PathVariable(value = "userId") Long userId,
                          @Valid @RequestBody NewCommentDto commentDto) {
        return service.add(userId, commentDto);
    }

    /*
    Обновить свой comment. Можно обновить только текст.
    Обновить можно любой свой comment. После обновления comment отправляется на модерацию администратора.
     */
    @PatchMapping
    public CommentDto update(@PathVariable(value = "userId") Long userId,
                             @Valid @RequestBody UpdateCommentDto commentDto) {
        return service.update(userId, commentDto);
    }

    // Полуить список своих comments с фильтрацией по статусу или без неё.
    @GetMapping
    public List<CommentDto> getAllByUserIdAndStatus(@PathVariable("userId") Long userId,
                                                    @RequestParam(value = "status", required = false) Status status,
                                                    @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        return service.getAllByUserIdAndStatus(userId, status, from, size);
    }

    // Удалить свой comment.
    @DeleteMapping
    public String delete(@PathVariable(value = "userId") Long userId,
                         @RequestParam(value = "commentId") Long commentId) {
        return service.delete(userId, commentId);
    }
}