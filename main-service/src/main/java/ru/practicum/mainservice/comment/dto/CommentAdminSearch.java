package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.request.model.Status;

import java.time.LocalDateTime;
import java.util.Set;

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