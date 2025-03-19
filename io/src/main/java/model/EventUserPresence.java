package model;

import javax.persistence.*;

@Entity
@Table(name = "event_user_presence")
public class EventUserPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idEvent", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @Column(name = "presence")
    private Boolean presence;

    // Konstruktor bezargumentowy
    public EventUserPresence() {}

    // Konstruktor z argumentami
    public EventUserPresence(Event event, User user, Boolean presence) {
        this.event = event;
        this.user = user;
        this.presence = presence;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }
}
