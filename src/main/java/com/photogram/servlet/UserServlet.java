package com.photogram.servlet;

import com.photogram.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/users")
public class UserServlet extends HttpServlet {


    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var printWriter = resp.getWriter()) {
            printWriter.println("<h1>User List:</h1>");
            printWriter.println("<ul>");
            userService.findAll().forEach(
                    userDto -> printWriter.write("""
                            <li><a href="/posts?userId=%d">%s</a>
                            </li>
                            """.formatted(userDto.getId(), userDto.getUsername())));
        }
    }
}
