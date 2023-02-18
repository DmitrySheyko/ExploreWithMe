package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.user.dto.UserShortDto;

/**
 * Class of dto for {@link Comment} entity.
 * Used for return information about {@link Comment} entity.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private UserShortDto user;
    private EventShortDto event;
    private String text;
    private String created;
    private String status;
}