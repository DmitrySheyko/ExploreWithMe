package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import ru.practicum.mainservice.compilation.model.Compilation;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Class of dto for {@link Compilation} entity.
 * Used for getting information for creating new {@link Compilation}.
 *
 * @author DmitrySheyko
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private boolean pinned;

    @NotBlank (message = "Compilation title shouldn't be blank")
    private String title;
}