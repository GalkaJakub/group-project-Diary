package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

/**
 *
 * @author jakub
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            System.out.println("OTRZYMANO ŻĄDANIE LOGOWANIA");

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            System.out.println(" Login: " + login);
            System.out.println(" Password: " + password);
            
            User user = null;
            try {
                user = User.authentication(login, password);
            } catch (Exception e) {
                System.out.println("🚨 Błąd w User.authentication():");
                e.printStackTrace(); // Wypisujemy błąd w logach Payara
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"status\": \"error\", \"message\": \"Błąd podczas sprawdzania użytkownika\"}");
                out.flush();
                return;
            }

            if (user != null) {
                System.out.println("Zalogowano użytkownika: " + user.getFirstName() + " " + user.getSurename());
                
                char userType;
                if(user.getUserType() == 'e'){
                    Employee employee = model.getCurrentEmployee();
                    userType = employee.getTypeEmployee();
                }
                
                else{
                    userType = user.getUserType();
                }
                
                out.print("{\"status\": \"success\", \"message\": \"Zalogowano\", \"firstName\": \"" 
                          + user.getFirstName() + "\", \"surename\": \"" + user.getSurename() 
                          + "\", \"role\": \"" + userType + "\"}");
            } else {
                System.out.println(" użytkownik nie istnieje lub błędne hasło");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"status\": \"error\", \"message\": \"Błędne dane logowania\"}");
            }

        } catch (Exception e) {
            System.out.println("Błąd:");
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Wewnętrzny błąd serwera\"}");
        }

        out.flush();
    }
}
