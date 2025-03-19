package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Employee;
import model.MyModel;
import model.Salary;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/SalaryServlet")
public class SalaryServlet extends HttpServlet {

    private static final MyModel model = MyModel.getModel();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to JSON and encoding to UTF-8.
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Retrieve the current logged-in user from the model.
            User currentUser = model.getCurrentUser();
            if (currentUser == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"status\":\"error\", \"message\":\"User not logged in.\"}");
                return;
            }

            List<Salary> salaries;

            Employee currentEmployee = model.getCurrentEmployee();
            if ("Admin".equals(currentUser.getFirstName())) {
                salaries = currentEmployee.getAllSalaries();
            }
            else if (currentUser.getUserType() == 'e') {
                if (currentEmployee == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.print("{\"status\":\"error\", \"message\":\"Employee details not found.\"}");
                    return;
                }
                salaries = currentEmployee.getSalariesOfSpecificEmployee(currentEmployee);
            }
            else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"status\":\"error\", \"message\":\"Access denied.\"}");
                return;
            }
            System.out.println("bbbbbbbbbbbbbbbbbbbbbb");

            // Build the JSON response.
            StringBuilder json = new StringBuilder();
            json.append("{\"status\":\"success\", \"salaries\": [");
            for (int i = 0; i < salaries.size(); i++) {
                Salary salary = salaries.get(i);
                String pesel = salary.getEmployee().getPeselNumber();
                String amount = salary.getAmount().toString();
                json.append("{\"pesel\":\"").append(pesel)
                    .append("\", \"salary\":").append(amount).append("}");
                if (i < salaries.size() - 1) {
                    json.append(", ");
                }
                System.out.println("aaaaaaaaaaaaaaaaaaa");

                System.out.println(pesel + "    " + salary);
            }
            System.out.println("cccccccccccccccc");
            json.append("]}");
            System.out.println(json);
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
