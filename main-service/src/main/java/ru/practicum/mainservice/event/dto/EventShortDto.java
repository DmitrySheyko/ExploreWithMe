package ru.practicum.mainservice.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.user.dto.UserShortDto;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
//    private Integer views;
}
