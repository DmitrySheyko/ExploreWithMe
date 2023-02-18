package ru.practicum.mainservice.user.dto;

import lombok.*;
import ru.practicum.mainservice.user.model.User;

/**
 * Class of short version dto for {@link User} entity
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}