package model;

import org.example.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class MyModel {

    static MyModel model = new MyModel();

    // obecnie zalogowany uzytkownik w sesji
    private User currentUser;

    private Student currentStudent = null;
    private Parent currentParent = null;
    private Employee currentEmployee = null;

    private PasswConverter converter = new PasswConverter();

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public Parent getCurrentParent() {
        return currentParent;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public static MyModel getModel() {
        return model;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public void setCurrentParent(Parent currentParent) {
        this.currentParent = currentParent;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public User getCurrentUser() {
        return currentUser;
    }


    public PasswConverter getConverter() {
        return converter;
    }


    public void loggedTypeUser(User user) {

        Long idUser = user.getIdUser();
        Session session = HibernateUtil.getSessionFactory().openSession(); // Otwieramy sesję

        try
        {
            this.currentUser = user;
            if(user.getUserType() == 's')
            {
                currentStudent = new Student();
                String hql = "FROM Student WHERE idUser = :id";
                currentStudent = (Student) session.createQuery(hql, Student.class).setParameter("id", idUser).uniqueResult();
                Hibernate.initialize(currentStudent.getGradesList()); // Wymuszenie załadowania listy

            }
            if (user.getUserType() == 'p') {
                currentParent = new Parent();
                String hql = "FROM Parent WHERE idUser = :id";
                currentParent = (Parent) session.createQuery(hql, Parent.class).setParameter("id", idUser).uniqueResult();

            }
            if (user.getUserType() == 'e') {
                currentEmployee = new Employee();
                String hql = "FROM Employee WHERE idUser = :id";
                currentEmployee = (Employee) session.createQuery(hql, Employee.class).setParameter("id", idUser).uniqueResult();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }
    }

    public MyModel() {
    }

    ;

    public static User getUserByID(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = null;
        try
        {
            String hql = "FROM User u WHERE u.idUser = :id";

            user = (User) session.createQuery(hql, User.class).setParameter("id", id).uniqueResult();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return user;
    }

    public static List<SchoolClass> getAllClasses() {
        List<SchoolClass> classes = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM SchoolClass c ORDER BY c.name";
            classes = session.createQuery(hql, SchoolClass.class).list();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }
        return classes;
    }

    public static List<User> getAllEmployees(char type) {
        List<User> teachers = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM User u JOIN Employee e ON u.id=e.id WHERE e.typeEmployee=" + type;
            teachers = session.createQuery(hql, User.class).list();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }
        return teachers;
    }

    public static List<Event> getScheduleForSpecificUser(User user) {
        List<Event> schedule = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql = "FROM Event e WHERE :user MEMBER OF e.participantsList";

            schedule = session.createQuery(hql, Event.class)
                    .setParameter("user", user)
                    .list();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return schedule;
    }

    public static SchoolClass getSchoolClassByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SchoolClass schoolClass = null;
        try
        {
            String hql = "FROM SchoolClass s WHERE s.name = :name";

            schoolClass= (SchoolClass) session.createQuery(hql, SchoolClass.class)
                    .setParameter("name", name)
                    .uniqueResult();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return schoolClass;
    }

    public static List<Event> getScheduleForClass(SchoolClass schoolClass) {
        List<Event> schedule = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {

            // Pobierz wszystkich uczniów z danej klasy
            List<User> students = session.createQuery("FROM Student s WHERE s.schoolClass = :schoolClass", User.class)
                    .setParameter("schoolClass", schoolClass)
                    .list();

            // Zapytanie, które pobierze wszystkie wydarzenia, w których bierze udział przynajmniej jeden uczeń z tej klasy
            String hql = "FROM Event e WHERE :students MEMBER OF e.participantsList";

            //  Wykonaj zapytanie dla każdego ucznia
            for (User student : students) {
                List<Event> eventsForStudent = session.createQuery(hql, Event.class)
                        .setParameter("students", student)
                        .list();
                // Dodajemy tylko te wydarzenia, które mają wszystkich uczniów klasy
                for (Event event : eventsForStudent) {
                    if (event.getParticipantsList().containsAll(students)) {
                        schedule.add(event); // Dodajemy wydarzenie, które spełnia warunek
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return schedule;  // Zwracamy listę wydarzeń
    }

    public  List<Student> getStudents()
    {
        List<Student> users = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql = "FROM Student s";
            users = session.createQuery(hql, Student.class).list();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return users;
    }

    public  List<Parent> getParents()
    {
        List<Parent> users = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql2 = "FROM Parent p";
            users = session.createQuery(hql2, Parent.class).list();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return users;
    }



    public  List<Employee> getEmployees()
    {
        List<Employee> users = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql3 = "FROM Employee e";
            users = session.createQuery(hql3, Employee.class).list();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return users;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String hql = "FROM User";
            users = session.createQuery(hql, User.class).list();
        } catch (Exception e) {
            System.out.println("❌ Błąd pobierania użytkowników: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }

        return users;
    }

    public static List<Subject> getSubjects()
    {
        List<Subject> subjects = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql2 = "FROM Subject s";
            subjects = session.createQuery(hql2, Subject.class).list();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return subjects;
    }

    public float calculateAverage(Student student) {

        float totalWeightedGrades = 0; // Suma ważona ocen
        int totalWeight = 0; // Suma wag ocen (GradePower)

        // Pobranie listy ocen z modelu
        List<Grade> grades = student.getGradesList();
        if(grades.isEmpty())
        {
            System.out.println("PUSTA");
        }

        if (!grades.isEmpty()) { // Sprawdzenie, czy lista nie jest pusta
            for (Grade g : grades) {
                System.out.println(g.getGrade());
                totalWeightedGrades += g.getGrade() * g.getGradePower(); // Dodaj ocenę pomnożoną przez wagę
                totalWeight += g.getGradePower(); // Dodaj wagę oceny
            }

        }

        return totalWeight > 0 ? totalWeightedGrades / totalWeight : 0;
    }

    public User getUserById(Long id) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    User user = null;

    try {
        user = session.get(User.class, id);
        if (user == null) {
            System.out.println("Błąd: Nie znaleziono wiadomości o ID " + id);
        }
    } catch (Exception e) {
        System.out.println("Błąd pobierania użytkownika o ID " + id + ": " + e.getMessage());
        e.printStackTrace();
    } finally {
        session.close();
    }

    return user;
    }

    public Message getMessageById(Long messageId) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Message message = null;

    try {
        message = session.get(Message.class, messageId);
        if (message == null) {
            System.out.println("Błąd: Nie znaleziono wiadomości o ID " + messageId);
        }
    } catch (Exception e) {
        System.out.println("Błąd pobierania wiadomości:");
        e.printStackTrace();
    } finally {
        session.close();
    }

    return message;
}
    public float calculateSpecificAverage(Student student, Subject subject)
    {
        float totalWeightedGrades = 0; // Suma ważona ocen
        int totalWeight = 0; // Suma wag ocen (GradePower)

        // Pobranie listy ocen z modelu
        List<Grade> grades = new ArrayList<Grade>();

        for(Grade grade : student.getGradesList())
        {
            if(grade.getSubject().equals(subject))
            {
                grades.add(grade);
            }
        }


        if(grades.isEmpty())
        {
            System.out.println("PUSTA");
        }

        if (!grades.isEmpty()) { // Sprawdzenie, czy lista nie jest pusta
            for (Grade g : grades) {
                System.out.println(g.getGrade());
                totalWeightedGrades += g.getGrade() * g.getGradePower(); // Dodaj ocenę pomnożoną przez wagę
                totalWeight += g.getGradePower(); // Dodaj wagę oceny
            }

        }

        return totalWeight > 0 ? totalWeightedGrades / totalWeight : 0;
    }
    public static Event getEventInfo(Long eventId) {
        Event event = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            String hql = "FROM Event e LEFT JOIN FETCH e.participantsList WHERE e.idEvent = :eventId";

            event = session.createQuery(hql, Event.class).setParameter("eventId", eventId).getSingleResult();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return event;
    }
}
