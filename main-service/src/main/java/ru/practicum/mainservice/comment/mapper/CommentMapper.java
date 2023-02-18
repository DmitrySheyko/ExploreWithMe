package ru.practicum.mainservice.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.comment.model.CommentAdminSearch;
import ru.practicum.mainservice.comment.dto.CommentAdminSearchDto;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.event.eventMapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper class for {@link Comment} entity.
 *
 * @author DmitrySheyko
 */
@Component
public class CommentMapper {
    private static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(User user, Event event, LocalDateTime created, String text) {
        return Comment.builder()
                .user(user)
                .event(event)
                .text(text)
                .created(created)
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(UserMapper.toShortDto(comment.getUser()))
                .event(EventMapper.toShortDto(comment.getEvent()))
                .text(comment.getText())
                .created(dateTimeToString(comment.getCreated()))
                .status(comment.getStatus().name())
                .build();
    }

    public static CommentAdminSearch toAdminSearch(CommentAdminSearchDto searchDto) {
        return CommentAdminSearch.builder()
                .users(searchDto.getUsers())
                .events(searchDto.getEvents())
                .text(searchDto.getText())
                .rangeStart(toLocalDateTime(searchDto.getRangeStart()))
                .rangeEnd(toLocalDateTime(searchDto.getRangeEnd()))
                .status(searchDto.getStatus())
                .build();
    }

    private static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_PATTERN);
    }

    private static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, DATE_TIME_PATTERN);
    }
}