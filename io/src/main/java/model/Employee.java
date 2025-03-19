package model;

import com.google.protobuf.Internal;
import org.example.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee extends User {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmployee;
    */

    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @Column(name="houseNumber")
    private String houseNumber;

    @Column(name="apartmentNumber")
    private String apartmentNumber;

    @Column(name="postCode")
    private String postCode;

    @Column(name="typeEmployee")
    private char typeEmployee;

    @Column(name="phoneNumber")
    private String phoneNumber;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Salary> salariesList;

    @ManyToMany
    @JoinTable(
            name = "employee_subject",
            joinColumns = @JoinColumn(name = "idEmployee"),
            inverseJoinColumns = @JoinColumn(name = "idSubject")
    )
    private List<Subject> subjectsList;

    @OneToOne(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    @ManyToMany(mappedBy = "participantsList")
    private List<Event> eventsForEmployee;

    // Konstruktor bezargumentowy
    public Employee() {}

    // Konstruktor z argumentami
    public Employee(String firstName, String lastName, String pesel, String login, String password, char userType, String city, String street, String houseNumber, String apartmentNumber, String postCode, char typeEmployee, String phoneNumber, List<Salary> salariesList, List<Subject> subjectsList, SchoolClass schoolClass) {
        this.setFirstName(firstName);
        this.setSurename(lastName);
        this.setPeselNumber(pesel);
        this.setLogin(login);
        this.setPassword(password);
        this.setUserType(userType);
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.postCode = postCode;
        this.typeEmployee = typeEmployee;
        this.phoneNumber = phoneNumber;
        this.salariesList = salariesList;
        this.subjectsList = subjectsList;
        this.schoolClass = schoolClass;
    }

    // Gettery i settery
    /*
    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }
    */

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public char getTypeEmployee() {
        return typeEmployee;
    }

    public void setTypeEmployee(char typeEmployee) {
        this.typeEmployee = typeEmployee;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Salary> getSalariesList() {
        return salariesList;
    }

    public void setSalariesList(List<Salary> salariesList) {
        this.salariesList = salariesList;
    }

    public List<Subject> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<Subject> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public List<Event> getEventsForEmployee() {
        return eventsForEmployee;
    }

    public void setEventsForEmployee(List<Event>  eventsForEmployee) {
        this.eventsForEmployee = eventsForEmployee;
    }


    public Boolean addGrade(int grade, int gradePower, Employee teacher, Subject subject, Student student, String description) {
        
        if (this.typeEmployee != 't') {
            return false; // Jeśli użytkownik nie jest nauczycielem, nie może dodać oceny.
        }

        // Walidacja danych wejściowych
        if (grade < 1 || grade > 6) { // Przykładowy zakres ocen (1-6)
            throw new IllegalArgumentException("Ocena musi być w zakresie 1-6.");
        }
        if (teacher == null || subject == null || student == null) {
            throw new IllegalArgumentException("Nauczyciel, przedmiot i uczeń nie mogą być null.");
        }

        // Tworzenie obiektu Grade
        Grade newGrade = new Grade(grade, gradePower, LocalDateTime.now(), teacher, subject, student, description);

        // Zapis do bazy danych
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(newGrade);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Zwracamy false zamiast rzucać wyjątek
        }
    }


    public Boolean addEvent(LocalDateTime start, LocalDateTime end, String topic, String descr, int schoolClass, List<User> paxs, Subject sub)
    {
        Boolean result = false;
        Event newEvent = new Event(start, end, topic, descr, schoolClass, paxs, null, sub);
        // Zapis wydarzenia w bazie danych
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            session.beginTransaction();
            session.persist(newEvent);
            session.getTransaction().commit();
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add a new event.");
        }
        return result;

    }

    public Boolean addUser(String name, String surname, String pesel, char type, String login, String password, char typeEmployee)
    {
        Boolean result = false;
        MyModel model = MyModel.getModel();

        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
        switch (type)
        {
            case 's':
                User newStudent = new Student(name, surname, pesel, login, model.getConverter().convertToDatabaseColumn(password), type, null, null);
                session.beginTransaction();
                session.persist(newStudent);
                session.getTransaction().commit();
                result = true;
                break;

            case 'p':
                User newParent = new Parent(name, surname, pesel, login, model.getConverter().convertToDatabaseColumn(password), type, null, null, null, null, null, null);
                session.beginTransaction();
                session.persist(newParent);
                session.getTransaction().commit();
                result = true;
                break;

            case 'e':
                User newEmployee = new Employee(name, surname, pesel, login, model.getConverter().convertToDatabaseColumn(password), type, null, null, null, null, null, typeEmployee, null, null, null, null);
                session.beginTransaction();
                session.persist(newEmployee);
                session.getTransaction().commit();
                result = true;
                break;
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add a new user.");
        }
        return result;

    }
    
    
    public List<Salary> getAllSalaries()
    {
        List<Salary> salaries = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql = "FROM Salary";
            salaries = session.createQuery(hql, Salary.class).list();
       
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return salaries;
    }
    
    public List<Salary> getSalariesOfSpecificEmployee(Employee employee)
    {
        List<Salary> salaries = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql = "FROM Salary WHERE employee = :employee";
            salaries = session.createQuery(hql, Salary.class).setParameter("employee", employee).list();
            
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return salaries;
    }
    

}