package ru.practicum.mainservice.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {

    @NotNull
    private Long id;

    @NotBlank
    private String text;
}