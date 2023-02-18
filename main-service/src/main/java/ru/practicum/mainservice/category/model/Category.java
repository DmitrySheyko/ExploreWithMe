package ru.practicum.mainservice.category.model;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;

import javax.persistence.*;

/**
 * Class of {@link Comment} entity.
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
}