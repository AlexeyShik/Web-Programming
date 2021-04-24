package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageServlet extends HttpServlet {
    private static class Message {
        private final String user;
        private final String text;

        Message(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }

    private static final List<Message> MESSAGES = new CopyOnWriteArrayList<>();  //  thread safe
    private static final Gson GSON = new Gson();  //  thread safe

    private static void saveMessage(String user, String text) {
        MESSAGES.add(new Message(user, text));
    }

    private static void writeJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json");
        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        writer.write(GSON.toJson(object));
        writer.flush();
    }

    private String getUserName(HttpServletRequest request) {
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) {
            userName = request.getParameter("user");
            request.getSession().setAttribute("user", userName);
        }
        return userName == null ? "" : userName;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        String userName = getUserName(request);
        switch (uri) {
            case "/message/auth":
                writeJson(response, userName);
                break;
            case "/message/add":
                saveMessage(userName, request.getParameter("text"));
                break;
            case "/message/findAll":
                writeJson(response, MESSAGES);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
}
