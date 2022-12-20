package ru.practicum.mainservice.request.model;

import lombok.*;
import ru.practicum.mainservice.event.model.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "event_id")
    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "requester_id")
    private Long requester;
    @Column(name = "status")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return id == request.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}