package org.example;

import com.mysql.cj.Messages;
import java.math.BigDecimal;
import java.time.LocalDate;
import model.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    //static HibernateUtil hibernateUtil = new HibernateUtil();

    public static void main(String[] args) {


        Boolean statusLoadData = false;
        statusLoadData = loadTestData();
        
      
        
        
        //User user_testowy = User.authentication("Bartłomiej","Łasica");

    // ************************************************

    }


    public static boolean loadTestData()
    {
        Boolean success = false;

        MyModel model = MyModel.getModel();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try
        {
            session.beginTransaction();
            SchoolClass class1a = new SchoolClass();
            SchoolClass class2a = new SchoolClass();
            class1a.setName("1a");
            class2a.setName("2a");

            Student student1 = new Student("Zbigniew", "Chmurka", "13212203615", "Zbigniew", model.getConverter().convertToDatabaseColumn("Chmurka"), 's', null, class1a);
            Student student2 = new Student("Ewelina", "Słoneczko", "11122007293", "Ewelina", model.getConverter().convertToDatabaseColumn("Słoneczko"), 's', null, class1a);
            Student student3 = new Student("Bartłomiej", "Łasica", "20111405680", "Bartłomiej", model.getConverter().convertToDatabaseColumn("Łasica"), 's',  null, class2a);
            Student student4 = new Student("Nataniel", "Północny", "80808080800", "Nataniel", model.getConverter().convertToDatabaseColumn("Północny"), 's',  null, class2a);

            Employee employee1 = new Employee("Marcel", "Drabik", "15220909742", "Marcel", model.getConverter().convertToDatabaseColumn("Drabik"), 'e', "Katowice", "Słoneczna",
                    "3", null, "41-500", 't', "123 456 789", null, null, class1a);

            Employee employee2 = new Employee("Jarosław", "Kot", "55234949742", "Jarosław", model.getConverter().convertToDatabaseColumn("Kot"),'e', "Gliwice", "Szkolna",
                    "1", null, "47-200", 't', "456 456 456", null, null, class2a);

            Employee employee3 = new Employee("Admin", "Admin", "33333333333", "Admin", model.getConverter().convertToDatabaseColumn("Admin"),'e', "Gliwice", "Cegły",
                    "7", null, "630-39", 'a', "123 123 123", null, null, null);

            class1a.setSupervisor(employee1);
            class1a.setStudents(Arrays.asList(student1, student2));
            class1a.setAmount(class1a.getStudents().size());

            class2a.setSupervisor(employee2);
            class2a.setStudents(Arrays.asList(student3, student4));
            class2a.setAmount(class2a.getStudents().size());

            Subject mathematics = new Subject("Mathemathics", null, null, null);
            mathematics.setTeacherList(Arrays.asList(employee1));
            employee1.setSubjectsList(Arrays.asList(mathematics));

            Subject informatics = new Subject("Informatics", null, null, null);
            informatics.setTeacherList(Arrays.asList(employee2));
            employee2.setSubjectsList(Arrays.asList(informatics));

            Parent parent1 = new Parent("Martyna", "Kazik", "15220909742", "Martyna", model.getConverter().convertToDatabaseColumn("Kazik"), 'p', "555 666 777",
                    "Żory", "80-68", "Giełżeckiego", "2", "3");
            parent1.setChildren(Arrays.asList(student1, student2));

            Parent parent2 = new Parent("Emilia", "Gady", "20100102538", "Emilia", model.getConverter().convertToDatabaseColumn("Gady"), 'p', "777 888 666",
                    "Zabrze", "44-74", "Daszyńskiego", "44", "4");
            parent2.setChildren(Arrays.asList(student3, student4));

            student1.setParent(Arrays.asList(parent1));
            student2.setParent(Arrays.asList(parent1));
            student3.setParent(Arrays.asList(parent2));
            student4.setParent(Arrays.asList(parent2));

            Grade grade1 = new Grade(5, 3, LocalDateTime.now(), employee1, mathematics, student1, "odpowiedz");
            Grade grade2 = new Grade(4, 5, LocalDateTime.now(), employee2, informatics, student1, "test");
            Grade grade3 = new Grade(4, 3, LocalDateTime.now(), employee1, mathematics, student1, "brak");

            student1.setGradesList(Arrays.asList(grade1, grade2, grade3));

            Grade grade4 = new Grade(3, 3, LocalDateTime.now(), employee1, mathematics, student2, "brak");
            Grade grade5 = new Grade(1, 5, LocalDateTime.now(), employee2, informatics, student2, "test");
            Grade grade6 = new Grade(2, 3, LocalDateTime.now(), employee1, mathematics, student2, "brak");
            
            
            student2.setGradesList(Arrays.asList(grade4, grade5, grade6));

            Event event1 = new Event(LocalDateTime.now(), LocalDateTime.now(), "Lekcja1", "opis", 12, Arrays.asList(student1, student2, employee1), new ArrayList<EventUserPresence>(), mathematics );
            Event event2 = new Event(LocalDateTime.now(), LocalDateTime.now(), "Lekcja1", "opis", 13, Arrays.asList(student4, student3, employee2), new ArrayList<EventUserPresence>(), informatics );
            
            Salary salary1 = new Salary(new BigDecimal(1000.00) , LocalDate.of(2024, 12, 10), "Pasek nr: 114", employee1);
            Salary salary2 = new Salary(new BigDecimal(2000.00) , LocalDate.of(2025, 01, 10), "Pasek nr: 116", employee1);
            Salary salary3 = new Salary(new BigDecimal(3000.00) , LocalDate.of(2024, 12, 10), "Pasek nr: 115", employee2);
            Salary salary4 = new Salary(new BigDecimal(4000.00) , LocalDate.of(2025, 01, 10), "Pasek nr: 117", employee3);
                        
            session.persist(class1a);
            session.persist(class2a);
            session.persist(mathematics);
            session.persist(informatics);
            session.persist(parent1);
            session.persist(parent2);
            session.persist(employee1);
            session.persist(employee2);
            session.persist(employee3);
            session.persist(event1);
            session.persist(event2);
            session.persist(salary1);
            session.persist(salary2);
            session.persist(salary3);
            session.persist(salary4);
   
        
            
            session.getTransaction().commit();

            success = true;

                    
        }
        catch (Exception e)
        {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Wystąpił błąd: " + e.getMessage());
            success = false;
        }
        finally {
            session.close();
        }

        return success;
    }
}