package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Event;
import model.User;
import model.MyModel;
import model.SchoolClass;
import model.Employee;
import model.Subject;

@WebServlet("/PlanEditorServlet")
public class PlanEditorServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            String json1 = sb.toString();
            Long subjectId = (long) Integer.parseInt(json1);
            if(subjectId!=-1)
            {
                Event event = MyModel.getEventInfo(subjectId);

                if (event == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Event not found");
                    return;
                }

                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"idEvent\":").append(event.getIdEvent()).append(",");
                json.append("\"topic\":\"").append(event.getTopic()).append("\",");
                json.append("\"description\":\"").append(event.getDescription()).append("\",");
                json.append("\"classNumber\":\"").append(event.getClassNumber()).append("\",");
                json.append("\"participantsList\":[");

                List<User> participants = event.getParticipantsList();
                for (int i = 0; i < participants.size(); i++) {
                    User user = participants.get(i);
                    json.append("{");
                    json.append("\"idUser\":").append(user.getIdUser()).append(",");
                    json.append("\"firstName\":\"").append(user.getFirstName()).append("\",");
                    json.append("\"surename\":\"").append(user.getSurename()).append("\"");
                    json.append("}");
                    if (i < participants.size() - 1) json.append(",");
                }

                json.append("]}");

                PrintWriter out = response.getWriter();
                out.print(json.toString());
                out.flush();
            }
            else if(subjectId == -1)
            {
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"idEvent\":").append(-1).append(",");
                json.append("\"topic\":\" \",");
                json.append("\"description\":\" \",");
                json.append("\"classNumber\":\" \",");
                json.append("\"participantsList\":[");


                json.append("{");
                json.append("\"idUser\":").append(0).append(",");
                json.append("\"firstName\":\" \",");
                json.append("\"surename\":\" \"");
                json.append("}");

                json.append("]}");

                PrintWriter out = response.getWriter();
                out.print(json.toString());
                out.flush();
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}

