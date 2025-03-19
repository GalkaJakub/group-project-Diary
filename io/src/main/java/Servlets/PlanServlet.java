package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Event;
import model.SchoolClass;
import model.MyModel;
import model.User;
import model.Subject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/PlanServlet")
public class PlanServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Pobranie listy klas
        List<SchoolClass> schoolClasses = MyModel.getAllClasses();
        List<String> classes = schoolClasses.stream()
                .map(SchoolClass::getName)
                .collect(Collectors.toList());

        List<User> employees = MyModel.getAllEmployees('t');
        List<String> teachers = employees.stream()
                .map(user -> user.getIdUser() + " " + user.getFirstName() + " " + user.getSurename())
                .collect(Collectors.toList());

        // Ręczne tworzenie JSON w formacie String
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");

        // Dodanie klas
        jsonResponse.append("\"classes\": [");
        for (int i = 0; i < classes.size(); i++) {
            jsonResponse.append("\"").append(classes.get(i)).append("\"");
            if (i < classes.size() - 1) {
                jsonResponse.append(", ");
            }
        }
        jsonResponse.append("], ");

        // Dodanie nauczycieli
        jsonResponse.append("\"teachers\": [");
        for (int i = 0; i < teachers.size(); i++) {
            jsonResponse.append("\"").append(teachers.get(i)).append("\"");
            if (i < teachers.size() - 1) {
                jsonResponse.append(", ");
            }
        }
        jsonResponse.append("]");

        jsonResponse.append("}");

        // Wysyłanie odpowiedzi
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        try {
            // Odczytanie danych JSON z ciała żądania
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();  // Teraz masz dane JSON w postaci String
            String selectedClass = json;

            // Teraz masz wartość selectedOption i możesz jej używać w swojej logice
            List<Event> schedule = new ArrayList<>();
            if (selectedClass != null) {
                int spaceIndex = selectedClass.indexOf(' ');
                if (spaceIndex == -1) {
                    schedule = MyModel.getScheduleForClass(MyModel.getSchoolClassByName(selectedClass));
                } else {
                    schedule = MyModel.getScheduleForSpecificUser(MyModel.getUserByID((long) Integer.parseInt(selectedClass.substring(1, spaceIndex))));
                }
            }
            ArrayList<Subject> subjects = new ArrayList<>();
            for(Event eve : schedule)
            {
                subjects.add(eve.getSubject());
            }

            ArrayList<String> subjectNames = new ArrayList<>();
            for(Subject sub : subjects)
            {
                subjectNames.add(sub.getName());
            }

            ArrayList<Long> eventIds = new ArrayList<>();
            for(Event eve : schedule)
            {
                eventIds.add(eve.getIdEvent());
            }

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{");

            // Dodanie klas
            jsonResponse.append("\"eveId\": [");
            for (int i = 0; i < eventIds.size(); i++) {
                jsonResponse.append(eventIds.get(i)).append(",");
            }
            for(int i=0; i< 40-subjectNames.size(); i++) {
                jsonResponse.append(-1);
                if(i!=40-subjectNames.size()-1)
                {
                    jsonResponse.append(",");
                }
            }
            jsonResponse.append("], ");
            // Dodanie nauczycieli
            jsonResponse.append("\"subjectNames\": [");
            for (int j = 0; j < subjectNames.size(); j++) {
                jsonResponse.append("\"").append(subjectNames.get(j)).append("\",");
            }
            for(int j=0; j< 40-subjectNames.size(); j++) {
                jsonResponse.append("\"").append("Brak zajęć").append("\"");
                if(j!=40-subjectNames.size()-1)
                {
                    jsonResponse.append(",");
                }
            }
            jsonResponse.append("]");

            jsonResponse.append("}");

            // Wysyłanie odpowiedzi
            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Błąd:");
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Wewnętrzny błąd serwera\"}");
        }
    }


}
