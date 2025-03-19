package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SchoolClass;
import model.MyModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.HibernateUtil;
import org.hibernate.Session;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/GetAllClassesServlet")
public class GetAllClassesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Retrieve all classes using your model.
            List<SchoolClass> classes = MyModel.getAllClasses();
            // Build JSON response.
            JsonObject json = new JsonObject();
            json.addProperty("status", "success");
            JsonArray jsonClasses = new JsonArray();
            for (SchoolClass sc : classes) {
                JsonObject classObj = new JsonObject();
                // Assuming SchoolClass has getId() and getName() methods.
                classObj.addProperty("id", sc.getIdSchoolClass());
                classObj.addProperty("name", sc.getName());
                jsonClasses.add(classObj);
            }
            json.add("classes", jsonClasses);
            out.print(json.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("status", "error");
            errorJson.addProperty("message", "Internal server error.");
            out.print(errorJson.toString());
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}
