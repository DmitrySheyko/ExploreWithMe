package ru.practicum.mainservice.comment.model;

import lombok.*;
import ru.practicum.mainservice.request.model.Status;

import java.time.LocalDateTime;
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
public class CommentAdminSearch {
    private Set<Long> users;
    private Set<Long> events;
    private String text;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Set<Status>  status;
}