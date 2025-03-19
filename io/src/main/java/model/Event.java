package model;

import org.example.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scheduleEvents")
@Inheritance(strategy = InheritanceType.JOINED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description")
    private String description;

    @Column(name = "classNumber")
    private int classNumber;

    @Column(name = "schoolClass")
    private String schoolClass;

    @ManyToMany
    @JoinTable(
            name = "events_users",
            joinColumns = @JoinColumn(name = "idEvent"),
            inverseJoinColumns = @JoinColumn(name = "idUser")
    )
    private List<User> participantsList;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventUserPresence> eventUserPresences;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSubject")
    private Subject subject;

    // Default constructor
    public Event() {
    }

    // Constructor with parameters
    public Event(LocalDateTime startTime, LocalDateTime endTime, String topic, String description,
                 int classNumber, List<User> participantsList, List<EventUserPresence> eventUserPresences, Subject subject) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
        this.description = description;
        this.classNumber = classNumber;
        this.participantsList = participantsList;
        this.eventUserPresences = eventUserPresences;
        this.subject = subject;
    }

    // Getter and Setter for idEvent
    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    // Getter and Setter for startTime
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // Getter and Setter for endTime
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Getter and Setter for topic
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for classNumber
    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    // Getter and Setter for participantsList
    public List<User> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(List<User> participantsList) {
        this.participantsList = participantsList;
    }

    // Getter and Setter for presenceList
    public List<EventUserPresence> getPresenceList() {
        return eventUserPresences;
    }

    public void setPresenceList(List<EventUserPresence> presenceList) {
        this.eventUserPresences = presenceList;
    }

    // Getter and Setter for subject
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }



}