package ru.practicum.mainservice.comment.dto;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class of dto for {@link Comment} entity.
 * Used for getting information for updating {@link Comment}.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {

    @NotNull(message = "Comment id shouldn't be null")
    private Long id;

    @NotBlank(message = "Text of comment shouldn't be blank")
    @Size(max = 1000, message = "Max length of text of comment = 1000")
    private String text;
}