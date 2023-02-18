package ru.practicum.mainservice.user.model;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.request.model.Request;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of {@link User} entity
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "requester")
    private List<Request> requestsSet = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentsList = new ArrayList<>();
}