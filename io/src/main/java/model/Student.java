package model;

import org.example.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends User {

    private static MyModel model = MyModel.getModel();

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStudent;
    */

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "idParent")

    @ManyToMany(mappedBy = "children", fetch = FetchType.EAGER)
    private List<Parent> rodzice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSchoolClass")
    private SchoolClass schoolClass;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<Grade> gradesList;

    // Konstruktor bezargumentowy
    public Student() {}


    // Konstruktor z argumentami
    public Student(List<Parent> rodzice, SchoolClass schoolClass) {
        this.rodzice = rodzice;
        this.schoolClass = schoolClass;
    }

    // Konstruktor inicjalizujacy
    public Student(String name, String surname, String peselNumber, String login, String password, char userType, List<Parent> rodzice, SchoolClass schoolClass) {
        this.setFirstName(name);
        this.setSurename(surname);
        this.setPeselNumber(peselNumber);
        this.setLogin(login);
        this.setPassword(password);
        this.setUserType(userType);
        this.rodzice = rodzice;
        this.schoolClass = schoolClass;
    }



    // Gettery i settery
    /*
    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }
     */

    public List<Parent> getParent() {
        return rodzice;
    }

    public void setParent(List<Parent> rodzice) {
        this.rodzice = rodzice;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<Grade> getGradesList() {
        return gradesList;
    }

    public void setGradesList(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    public List<Grade> getGradesFromDatabase()
    {
        List<Grade> grades = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try
        {
            String hql = "FROM Grade WHERE student = :student";
            grades = session.createQuery(hql, Grade.class).setParameter("student", this).list();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return grades;

    }

  
}