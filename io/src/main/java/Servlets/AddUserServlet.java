package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.Student;
import model.Employee;
import model.Parent;
import model.MyModel;
import model.SchoolClass;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         response.setContentType("application/json;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
             // Read the JSON body.
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = request.getReader().readLine()) != null) {
                 sb.append(line);
             }
             JsonObject jsonRequest = JsonParser.parseString(sb.toString()).getAsJsonObject();
             
             // Common fields for every user.
             String firstName = jsonRequest.get("firstName").getAsString();
             String lastName = jsonRequest.get("lastName").getAsString();
             String pesel = jsonRequest.get("pesel").getAsString();
             String login = jsonRequest.get("login").getAsString();
             String password = jsonRequest.get("password").getAsString();
             String convertedPassword = model.getConverter().convertToDatabaseColumn(password);
             String role = jsonRequest.get("role").getAsString().toLowerCase();
             
             SchoolClass schoolClass = null;
             if(jsonRequest.has("schoolClass")) {
                String className = jsonRequest.get("schoolClass").getAsString();
                if(!className.isEmpty()){
                    // Retrieve all classes and find the one matching the provided name.
                    List<SchoolClass> classes = MyModel.getAllClasses();
                    for(SchoolClass c : classes){
                        if(c.getName().equals(className)){
                            schoolClass = c;
                            break;
                        }
                    }
                }
}
             
             User newUser = null;
             
             if(role.equals("student")) {
                 // For student, the parent's list is not provided (set to null)
                 // and userType is always 's'
                 newUser = new Student(firstName, lastName, pesel, login, convertedPassword, 's', null, schoolClass);
             } else if(role.equals("teacher") || role.equals("admin")) {
                 // For teacher and admin, we need additional address/phone fields.
                 String city = jsonRequest.get("city").getAsString();
                 String street = jsonRequest.get("street").getAsString();
                 String houseNumber = jsonRequest.get("houseNumber").getAsString();
                 String apartmentNumber = jsonRequest.has("apartmentNumber") ? jsonRequest.get("apartmentNumber").getAsString() : null;
                 String postCode = jsonRequest.get("postCode").getAsString();
                 String phoneNumber = jsonRequest.get("phoneNumber").getAsString();
                 
                 // Determine the typeEmployee:
                 // for teacher, 't'; for admin, 'a'
                 char typeEmployee = role.equals("admin") ? 'a' : 't';
                 // For teacher, schoolClass is expected; for admin, it can be null.
                 newUser = new Employee(firstName, lastName, pesel, login, convertedPassword, 'e',
                                         city, street, houseNumber, apartmentNumber, postCode, typeEmployee,
                                         phoneNumber, null, null, schoolClass);
             } else if(role.equals("parent")) {
                 // For parent, we need address and phone details.
                 String phoneNumber = jsonRequest.get("phoneNumber").getAsString();
                 String city = jsonRequest.get("city").getAsString();
                 String street = jsonRequest.get("street").getAsString();
                 String houseNumber = jsonRequest.get("houseNumber").getAsString();
                 String apartmentNumber = jsonRequest.has("apartmentNumber") ? jsonRequest.get("apartmentNumber").getAsString() : null;
                 String postCode = jsonRequest.get("postCode").getAsString();
                 
                 newUser = new Parent(firstName, lastName, pesel, login, convertedPassword, 'p',
                                        phoneNumber, city, postCode, street, houseNumber, apartmentNumber);
             } else {
                 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 out.print("{\"status\":\"error\", \"message\":\"Invalid role.\"}");
                 return;
             }
             
             // Persist the new user.
             Session session = HibernateUtil.getSessionFactory().openSession();
             Transaction tx = session.beginTransaction();
             session.persist(newUser);
             tx.commit();
             session.close();
             
             out.print("{\"status\":\"success\"}");
         } catch(Exception e) {
             response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             out.print("{\"status\":\"error\", \"message\":\"Internal server error.\"}");
             e.printStackTrace();
         } finally {
             out.flush();
         }
    }
}
