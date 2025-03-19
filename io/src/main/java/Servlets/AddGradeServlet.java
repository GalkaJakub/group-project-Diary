package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import model.Student;
import model.Subject;
import model.User;
import model.MyModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/AddGradeServlet")
public class AddGradeServlet extends HttpServlet {

    private static final MyModel model = MyModel.getModel();

    // Helper method: retrieve a Student by ID using the list from model.getStudents()
    private Student getStudentById(int studentId) {
        List<Student> students = model.getStudents();
        for (Student s : students) {
            if (s.getIdUser() == studentId) {
                return s;
            }
        }
        return null;
    }

    // Helper method: retrieve a Subject by ID using the list from model.getSubjects()
    private Subject getSubjectById(int subjectId) {
        List<Subject> subjects = model.getSubjects();
        for (Subject subj : subjects) {
            if (subj.getIdSubject() == subjectId) {
                return subj;
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to JSON with UTF-8 encoding.
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Ensure that the logged-in user is a teacher.
            User currentUser = model.getCurrentUser();
            if (currentUser == null || currentUser.getUserType() != 'e') {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"status\":\"error\", \"message\":\"User not authorized.\"}");
                return;
            }
            Employee teacher = model.getCurrentEmployee();
            if (teacher == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"status\":\"error\", \"message\":\"Teacher details not found.\"}");
                return;
            }

            // Read parameters from the request.
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            int gradeValue = Integer.parseInt(request.getParameter("grade"));
            int gradePower = Integer.parseInt(request.getParameter("gradePower"));
            String description = request.getParameter("description");

            // Retrieve the student and subject using helper methods.
            Student student = getStudentById(studentId);
            Subject subject = getSubjectById(subjectId);
            if (student == null || subject == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\", \"message\":\"Invalid student or subject.\"}");
                return;
            }

            // Use the teacher’s business logic to add the grade.
            // (Note: This method is expected to perform any validations and persist the new grade.)
            boolean success = teacher.addGrade(gradeValue, gradePower, teacher, subject, student, description);
            if (success) {
                out.print("{\"status\":\"success\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Failed to add grade.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"Internal server error.\"}");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
