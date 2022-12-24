package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearch {
    private List<Long> users;
    private List<Integer> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
