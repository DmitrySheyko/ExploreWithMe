package ru.practicum.mainservice.category.dto;

import lombok.*;
import ru.practicum.mainservice.category.model.Category;

import javax.validation.constraints.NotBlank;

/**
 * Class of dto for {@link Category} entity.
 * Used for getting information for creating new {@link Category}.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = " Category name shouldn't be blank")
    private String name;
}