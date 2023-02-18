package ru.practicum.mainservice.user.dto;

import lombok.*;
import ru.practicum.mainservice.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Class of dto for {@link User} entity
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Positive(message = "User id should be positive")
    private Long id;

    @NotBlank(message = "User name shouldn't be blank")
    private String name;

    @NotNull(message = "User email shouldn't be null")
    @Email(message = "User email is incorrect")
    private String email;
}