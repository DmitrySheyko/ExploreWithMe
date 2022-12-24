package ru.practicum.mainservice.event.dto;

import lombok.*;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminSearchDto {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
}
