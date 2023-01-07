package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.request.model.Status;

import java.util.Set;

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