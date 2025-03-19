package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrade;

    @Column(name="grade")
    private int grade;

    @Column(name="gradePower")
    private int gradePower;

    @Column(name="date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeacher")
    private Employee teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSubject")
    private Subject subject;


    @ManyToOne()
    @JoinColumn(name = "idStudent" )
    private Student student;

    @Column(name="description")
    private String description;

    // Konstruktor bezargumentowy
    public Grade() {}

    // Konstruktor z argumentami
    public Grade(int grade, int gradePower, LocalDateTime date, Employee teacher, Subject subject, Student student, String description) {
        this.grade = grade;
        this.gradePower = gradePower;
        this.date = date;
        this.teacher = teacher;
        this.subject = subject;
        this.student = student;
        this.description = description;
    }

    // Gettery i settery
    public Long getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(Long idGrade) {
        this.idGrade = idGrade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGradePower() {
        return gradePower;
    }

    public void setGradePower(int gradePower) {
        this.gradePower = gradePower;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Employee getTeacher() {
        return teacher;
    }

    public void setTeacher(Employee teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }



    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
