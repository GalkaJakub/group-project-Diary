package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "schoolClasses")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSchoolClass;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSupervisor")
    private Employee supervisor;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students;

    public SchoolClass() {
    }

    public SchoolClass(String name, int amount, Employee supervisor, List<Student> students) {
        this.name = name;
        this.amount = amount;
        this.supervisor = supervisor;
        this.students = students;
    }

    // Getter and Setter for idSchoolClass
    public Long getIdSchoolClass() {
        return idSchoolClass;
    }

    public void setIdSchoolClass(Long idSchoolClass) {
        this.idSchoolClass = idSchoolClass;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for amount
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Getter and Setter for supervisor
    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    // Getter and Setter for students
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}