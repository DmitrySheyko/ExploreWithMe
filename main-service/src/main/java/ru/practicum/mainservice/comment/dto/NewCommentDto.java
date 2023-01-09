package ru.practicum.mainservice.comment.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    @NotNull(message = "Event id of comment shouldn't be null")
    private Long event;

    @NotBlank(message = "Text of comment shouldn't be blank")
    @Size(max = 1000, message = "Max length of text of comment = 1000")
    private String text;
}