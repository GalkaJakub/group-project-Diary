package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/MessagesServlet")
public class MessageServlet extends HttpServlet {

    private static final MyModel model = MyModel.getModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if ("getUsers".equals(action)) {
                List<User> users = model.getAllUsers();
                String json = convertUsersToJson(users);
                out.print(json);
                out.flush();
                return;
            }

            User currentUser = model.getCurrentUser(); 

            if (currentUser == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"status\": \"error\", \"message\": \"Użytkownik nie jest zalogowany\"}");
                out.flush();
                return;
            }

            List<Message> messages = currentUser.reloadMessages(currentUser);
            String json = convertMessagesToJson(messages); // Ręczne formatowanie JSON
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Wewnętrzny błąd serwera\"}");
            out.flush();
        }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("application/json");
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter();

    try {
        String action = request.getParameter("action");
        
        if ("markAsRead".equals(action)) {
            Long messageId = Long.parseLong(request.getParameter("id"));
            
            Message message = model.getMessageById(messageId); // Pobranie wiadomości

            if (message == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\": \"error\", \"message\": \"Nie znaleziono wiadomości\"}");
                out.flush();
                return;
            }

            boolean updated = message.changeStatus(); // Użycie metody w Message

            if (updated) {
                out.print("{\"status\": \"success\", \"message\": \"Wiadomość oznaczona jako przeczytana\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"status\": \"error\", \"message\": \"Błąd aktualizacji statusu\"}");
            }

            out.flush();
            return;
        }
        
        User sender = model.getCurrentUser();

        if (sender == null) {
            System.out.println("Błąd: Użytkownik niezalogowany");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"status\": \"error\", \"message\": \"Użytkownik nie jest zalogowany\"}");
            out.flush();
            return;
        }

        // Pobranie danych z żądania
        String recipientId = request.getParameter("recipient");
        String title = request.getParameter("title");
        String body = request.getParameter("message");

        System.out.println("Otrzymano nową wiadomość:");

        // Sprawdzenie, czy wszystkie dane są dostępne
        if (recipientId == null || title == null || body == null) {
            System.out.println("Błąd: Brak wymaganych danych");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"Brak wymaganych danych\"}");
            out.flush();
            return;
        }

        // Pobranie odbiorcy
        User recipient = model.getUserById(Long.parseLong(recipientId));

        if (recipient == null) {
            System.out.println("Błąd: Nie znaleziono odbiorcy o ID " + recipientId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"status\": \"error\", \"message\": \"Nie znaleziono odbiorcy\"}");
            out.flush();
            return;
        }

        sender.sendMessage(sender, recipient, title, body);

        out.print("{\"status\": \"success\", \"message\": \"Wiadomość wysłana\"}");
        out.flush();
    } catch (Exception e) {
        System.out.println("Błąd wysyłania wiadomości:");
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"status\": \"error\", \"message\": \"Błąd wysyłania wiadomości\"}");
        out.flush();
    }
}


    private String convertUsersToJson(List<User> users) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            json.append("{")
                .append("\"idUser\":").append(user.getIdUser()).append(",")
                .append("\"firstName\":\"").append(user.getFirstName()).append("\",")
                .append("\"surename\":\"").append(user.getSurename()).append("\",")
                .append("\"userType\":\"").append(user.getUserType()).append("\"")
                .append("}");
            if (i < users.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    private String convertMessagesToJson(List<Message> messages) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            json.append("{")
                .append("\"idMessage\":").append(msg.getIdMessage()).append(",")
                .append("\"sender\":\"").append(msg.getSender().getFirstName()).append(" ").append(msg.getSender().getSurename()).append("\",")
                .append("\"receiver\":\"").append(msg.getReceiver().getFirstName()).append(" ").append(msg.getReceiver().getSurename()).append("\",")
                .append("\"title\":\"").append(msg.getTitle()).append("\",")
                .append("\"message\":\"").append(msg.getMessageSource()).append("\",")
                .append("\"sentDate\":\"").append(msg.getSentDate().toString()).append("\",")
                .append("\"status\":\"").append(msg.getStatus()).append("\"")
                .append("}");
            if (i < messages.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}
