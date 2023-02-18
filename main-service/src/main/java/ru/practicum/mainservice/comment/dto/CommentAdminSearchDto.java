package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.request.model.Status;

import java.util.Set;

/**
 * Class of dto for {@link Comment} entity.
 * Used for getting information about search request for {@link Comment} from users with admin role.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAdminSearchDto {
    private Set<Long> users;
    private Set<Long> events;
    private String text;
    private String rangeStart;
    private String rangeEnd;
    private Set<Status> status;
}