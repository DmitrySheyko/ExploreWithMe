package ru.practicum.mainservice.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    @NonNull
    @Email
    private String email;
}