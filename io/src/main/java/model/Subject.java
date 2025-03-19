package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubject;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "subjectsList")
    private List<Employee> teacherList;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Column(name = "grades")
    private List<Grade> gradesList;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Column(name = "schedule")
    private List<Event> scheduleForSubject;

    // Default constructor
    public Subject() {
    }

    // Constructor with parameters
    public Subject(String name, List<Employee> teacherList, List<Grade> gradesList, List<Event> scheduleForSubject) {
        this.name = name;
        this.teacherList = teacherList;
        this.gradesList = gradesList;
        this.scheduleForSubject = scheduleForSubject;
    }

    // Getter and Setter for idSubject
    public Long getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Long idSubject) {
        this.idSubject = idSubject;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for teacherList
    public List<Employee> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Employee> teacherList) {
        this.teacherList = teacherList;
    }

    // Getter and Setter for gradesList
    public List<Grade> getGradesList() {
        return gradesList;
    }

    public void setGradesList(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    // Getter and Setter for scheduleForSubject
    public List<Event> getScheduleForSubject() {
        return scheduleForSubject;
    }

    public void setScheduleForSubject(List<Event> scheduleForSubject) {
        this.scheduleForSubject = scheduleForSubject;
    }
}