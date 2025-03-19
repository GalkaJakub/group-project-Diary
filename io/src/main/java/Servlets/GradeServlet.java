package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Grade;
import model.Student;
import model.Subject;
import model.User;
import model.MyModel;
import model.Parent;
import org.hibernate.Session;
import org.hibernate.Hibernate;
import org.example.HibernateUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/GradesServlet")
public class GradeServlet extends HttpServlet {

    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Session hibernateSession = null;
        try {
            // Open new Hibernate session
            hibernateSession = HibernateUtil.getSessionFactory().openSession();
            
            User currentUser = model.getCurrentUser();
            if (currentUser == null) {
                sendError(response, out, 401, "User not logged in.");
                return;
            }

            List<Student> students = loadStudentsWithGrades(hibernateSession, currentUser);
            String json = buildStudentJson(students);
            out.print(json);

        } catch (Exception e) {
            sendError(response, out, 500, "Internal server error.");
            e.printStackTrace();
        } finally {
            if (hibernateSession != null) {
                hibernateSession.close();
            }
            out.flush();
        }
    }

    private List<Student> loadStudentsWithGrades(Session session, User user) throws Exception {
        // Get student IDs first
        List<Long> studentIds = getStudentIds(user);
        
        if (studentIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch students with initialized collections using HQL
        return session.createQuery(
            "SELECT DISTINCT s FROM Student s " +
            "LEFT JOIN FETCH s.gradesList g " +
            "LEFT JOIN FETCH g.subject " +
            "WHERE s.idUser IN :ids", Student.class)
            .setParameter("ids", studentIds)
            .getResultList();
    }

    private List<Long> getStudentIds(User user) throws Exception {
        List<Long> ids = new ArrayList<>();
        switch (user.getUserType()) {
            case 's':
                Student s = model.getCurrentStudent();
                if (s != null) ids.add(s.getIdUser());
                break;
            case 'e':
                model.getStudents().forEach(st -> ids.add(st.getIdUser()));
                break;
            case 'p':
                Parent p = model.getCurrentParent();
                if (p != null) p.getChildren().forEach(st -> ids.add(st.getIdUser()));
                break;
            default:
                throw new Exception("Invalid user type");
        }
        return ids;
    }

    private String buildStudentJson(List<Student> students) {
        StringBuilder json = new StringBuilder();
        json.append("{\"status\":\"success\", \"students\": [");
        
        for (Student student : students) {
            json.append("{")
                .append("\"studentId\":").append(student.getIdUser())
                .append(", \"studentName\":\"").append(escapeJson(student.getFirstName()))
                .append(" ").append(escapeJson(student.getSurename())).append("\", ")
                .append("\"grades\": [");

            Map<Long, Map<String, Object>> subjects = new LinkedHashMap<>();

            for (Grade grade : student.getGradesList()) {
                Subject subject = grade.getSubject();
                subjects.computeIfAbsent(subject.getIdSubject(), k -> {
                    Map<String, Object> subj = new LinkedHashMap<>();
                    subj.put("subjectId", subject.getIdSubject());
                    subj.put("subjectName", subject.getName());
                    subj.put("grades", new ArrayList<Map<String, Object>>()); // Explicit generic type
                    subj.put("average", model.calculateSpecificAverage(student, subject));
                    return subj;
                });

                Map<String, Object> gradeData = new HashMap<>();
                gradeData.put("value", grade.getGrade());
                gradeData.put("power", grade.getGradePower());
                gradeData.put("description", grade.getDescription());

                // Get the list with proper typing
                List<Map<String, Object>> gradeList = 
                    (List<Map<String, Object>>) subjects.get(subject.getIdSubject()).get("grades");
                gradeList.add(gradeData);
            }

            subjects.values().forEach(subj -> {
                json.append("{")
                    .append("\"subject\":{")
                    .append("\"idSubject\":").append(subj.get("subjectId"))
                    .append(", \"name\":\"").append(escapeJson((String) subj.get("subjectName"))).append("\"")
                    .append("}, ")
                    .append("\"grades\": [");
                
                ((List<?>) subj.get("grades")).forEach(grade -> {
                    json.append("{")
                        .append("\"value\":").append(((Map<?, ?>) grade).get("value"))
                        .append(", \"power\":").append(((Map<?, ?>) grade).get("power"))
                        .append(", \"description\":\"").append(escapeJson((String) ((Map<?, ?>) grade).get("description")))
                        .append("\"},");
                });
                
                if (!((List<?>) subj.get("grades")).isEmpty()) {
                    json.setLength(json.length() - 1); // Remove trailing comma
                }
                
                json.append("], ")
                    .append("\"average\":").append(subj.get("average"))
                    .append("},");
            });
            
            if (!subjects.isEmpty()) {
                json.setLength(json.length() - 1); // Remove trailing comma
            }
            
            json.append("]},");
        }
        
        if (!students.isEmpty()) {
            json.setLength(json.length() - 1); // Remove trailing comma
        }
        
        json.append("]}");
        System.out.println(json);
        return json.toString();
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private void sendError(HttpServletResponse response, PrintWriter out, int code, String message) {
        response.setStatus(code);
        out.print("{\"status\":\"error\", \"message\":\"" + message + "\"}");
    }
}