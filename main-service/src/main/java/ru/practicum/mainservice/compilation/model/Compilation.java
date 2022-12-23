package ru.practicum.mainservice.compilation.model;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @ManyToMany(mappedBy = "compilations")
    List<Event> events = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "is_pinned")
    Boolean pinned;
    @Column(name = "title")
    String title;
}
