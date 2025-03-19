package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.MyModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/GetUsersServlet")
public class GetUsersServlet extends HttpServlet {
    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set content type to JSON with UTF-8 encoding.
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<User> users = model.getAllUsers();

            // Build the JSON response manually.
            StringBuilder json = new StringBuilder();
            json.append("{\"status\":\"success\", \"users\":[");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                json.append("{");
                json.append("\"userId\":").append(user.getIdUser()).append(", ");
                json.append("\"username\":\"").append(user.getLogin()).append("\", ");
                char userType = user.getUserType();
                String role;
                if (userType == 's') {
                    role = "student";
                } else if (userType == 'e') {
                    role = "teacher";
                } else if (userType == 'p') {
                    role = "parent";
                } else if (userType == 'a') {
                    role = "admin";
                } else {
                    role = "unknown";
                }
                json.append("\"role\":\"").append(role).append("\"");
                json.append("}");
                if (i < users.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("]}");
            out.print(json.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"Internal server error.\"}");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
