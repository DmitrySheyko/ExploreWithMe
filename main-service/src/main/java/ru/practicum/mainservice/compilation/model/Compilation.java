package ru.practicum.mainservice.compilation.model;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class of {@link Compilation} entity.
 *
 * @author DmitrySheyko
 */
@Entity
@Table(name = "compilations")
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "events_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    Set<Event> events = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Column(name = "is_pinned")
    Boolean pinned;

    @Column(name = "title")
    String title;
}