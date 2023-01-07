package ru.practicum.mainservice.user.model;

import lombok.*;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.request.model.Request;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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