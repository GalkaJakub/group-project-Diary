package model;

import javax.persistence.*;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTest;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSchoolEvent")
    private Event schoolEvent;

    // Default constructor
    public Test() {
    }

    // Constructor with parameters
    public Test(String name, String description, Event schoolEvent) {
        this.name = name;
        this.description = description;
        this.schoolEvent = schoolEvent;
    }

    // Getter and Setter for idTest
    public Long getIdTest() {
        return idTest;
    }

    public void setIdTest(Long idTest) {
        this.idTest = idTest;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for schoolEvent
    public Event getSchoolEvent() {
        return schoolEvent;
    }

    public void setSchoolEvent(Event schoolEvent) {
        this.schoolEvent = schoolEvent;
    }
}