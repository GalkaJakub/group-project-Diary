package model;

import javax.persistence.*;

@Entity
@Table(name = "substitutions")
public class Substitution extends Event {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubstitution;
    */

    @Column(name = "permission")
    private Boolean permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOldEvent")
    private Event oldEvent;

    @ManyToOne(fetch = FetchType.LAZY)  // Relacja wiele-do-jednego z Event (newEvent)
    @JoinColumn(name = "idNewEvent")
    private Event newEvent;

    public Substitution() {
    }

    public Substitution(Boolean permission, Event oldEvent, Event newEvent) {
        this.permission = permission;
        this.oldEvent = oldEvent;
        this.newEvent = newEvent;
    }

    // Getter and Setter for idSubstitution
    /*
    public Long getIdSubstitution() {
        return idSubstitution;
    }

    public void setIdSubstitution(Long idSubstitution) {
        this.idSubstitution = idSubstitution;
    }
     */

    // Getter and Setter for permission
    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    // Getter and Setter for oldEvent
    public Event getOldEvent() {
        return oldEvent;
    }

    public void setOldEvent(Event oldEvent) {
        this.oldEvent = oldEvent;
    }

    // Getter and Setter for newEvent
    public Event getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }
}