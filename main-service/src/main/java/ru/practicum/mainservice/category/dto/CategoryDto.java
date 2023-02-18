package ru.practicum.mainservice.category.dto;

import lombok.*;
import ru.practicum.mainservice.category.model.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Class of dto for {@link Category} entity.
 * Used for return information about {@link Category} entity.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
