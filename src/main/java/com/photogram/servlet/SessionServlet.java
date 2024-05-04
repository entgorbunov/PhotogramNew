package com.photogram.servlet;

import com.photogram.dto.userDto.UserDtoFromWeb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sessions")
public class SessionServlet extends HttpServlet {
    private static final String USER = "user";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageUserSession(req);
    }

    private void manageUserSession(HttpServletRequest req) {
        var session = req.getSession();
        UserDtoFromWeb user = (UserDtoFromWeb) session.getAttribute(USER);
        if (user == null) {
            user = createUserWithDefaultValues();
            session.setAttribute(USER, user);
        }
    }

    private UserDtoFromWeb createUserWithDefaultValues() {
        return UserDtoFromWeb.builder()
                .email("default@example.com")
                .name("Default User")
                .build();
    }
}
