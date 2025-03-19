package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.MyModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         response.setContentType("application/json;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
             // Read JSON request.
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = request.getReader().readLine()) != null) {
                 sb.append(line);
             }
             JsonObject jsonRequest = JsonParser.parseString(sb.toString()).getAsJsonObject();
             String username = jsonRequest.get("username").getAsString();

             // Find the user by username. (Assuming the user's login equals the username.)
             List<User> allUsers = model.getAllUsers();
             User userToDelete = null;
             for(User u : allUsers) {
                 if(u.getLogin().equals(username)) {
                     userToDelete = u;
                     break;
                 }
             }

             if(userToDelete == null) {
                 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                 out.print("{\"status\":\"error\", \"message\":\"User not found.\"}");
                 return;
             }

             // Delete the user.
             Session session = HibernateUtil.getSessionFactory().openSession();
             Transaction tx = session.beginTransaction();
             session.remove(userToDelete);
             tx.commit();
             session.close();

             Gson gson = new Gson();
             out.print(gson.toJson(new ResponseWrapper("success")));
         } catch(Exception e) {
             response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             out.print("{\"status\":\"error\", \"message\":\"Internal server error.\"}");
             e.printStackTrace();
         } finally {
             out.flush();
         }
    }

    private class ResponseWrapper {
         String status;
         ResponseWrapper(String status) {
              this.status = status;
         }
    }
}
