package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentAdminSearchDto;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.service.CommentServiceImpl;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

/**
 * Class of controller for {@link Comment} entity for users with Admin role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentServiceImpl service;

    //Получить полный список comments или выборку по параметрам.
    @GetMapping
    public List<CommentDto> search(@RequestParam(value = "users", required = false) Set<Long> users,
                                   @RequestParam(value = "events", required = false) Set<Long> events,
                                   @RequestParam(value = "text", required = false) String text,
                                   @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                   @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                   @RequestParam(value = "status", required = false) Set<Status> status,
                                   @RequestParam(value = "sort", required = false) String sort,
                                   @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        CommentAdminSearchDto adminSearchDto = CommentAdminSearchDto.builder()
                .users(users)
                .events(events)
                .text(text)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .status(status)
                .build();
        return service.search(adminSearchDto, from, size, sort);
    }

    // Опубликовать comment.
    @PatchMapping("/{commentId}/publish")
    public CommentDto publish(@PathVariable("commentId") Long commentId) {
        return service.publish(commentId);
    }

    // Отклонить comment.
    @PatchMapping("/{commentId}/reject")
    public CommentDto reject(@PathVariable("commentId") Long commentId) {
        return service.reject(commentId);
    }
}