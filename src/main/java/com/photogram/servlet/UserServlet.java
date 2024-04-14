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
        try (var writer = resp.getWriter()) {
            writer.write("<h1>Список пользователей:</h1>");
            writer.write("<ul>");
            userService.findAll().forEach(userDto -> writer.write("""
                    <li>
                        <a href="/posts?userId=%d">%s
                    </li> 
                    """.formatted(userDto.getId(), userDto.getBio())));
            writer.write("</ul>");

        }
    }
}
