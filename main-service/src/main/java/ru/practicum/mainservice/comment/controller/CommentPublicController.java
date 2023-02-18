package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.service.CommentServiceImpl;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Class of controller for {@link Comment} entity for users with public role
 *
 * @author DmitrySheyko
 */
@RestController
@RequestMapping("/public/comments")
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentServiceImpl service;

    /*
    Получить все опубликованные comments
    или получить все оплубликованные comments, относщиеся к определенному event.
     */
    @GetMapping("/{eventId}")
    public List<CommentDto> getAllPublishedByEvent(@PathVariable("eventId") Long eventId,
                                                   @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        return service.getAllPublishedByEventId(eventId, from, size);
    }
}