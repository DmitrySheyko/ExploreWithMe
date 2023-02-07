package ru.practicum.mainservice.event.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.location.model.Location;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Column(name = "annotation")
    @Length(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "creates_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    @Length(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @OneToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "is_paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "is_request_moderated")
    private Boolean requestModeration;

    @Column(name = "state")
    private State state;

    @Column(name = "title")
    @Length(min = 3, max = 120)
    private String title;

    @Transient
    private Integer views;

    @OneToMany(mappedBy = "event")
    private List<Request> requestsSet = new ArrayList<>();

    @ManyToMany(mappedBy = "events")
    List<Compilation> compilations = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    private List<Comment> commentList = new ArrayList<>();
}