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
public class EventFullDto {
    @Length(min = 20, max = 2000)
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    @Length(min = 20, max = 7000)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    @Length(min = 3, max = 120)
    private String title;
//    private Integer views;
}
