package com.photogram.servlet;

import com.photogram.daoException.DaoException;
import com.photogram.dto.UserDto;
import com.photogram.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var writer = resp.getWriter()) {
            try {
                List<UserDto> users = userService.findAll();
                writer.write("<h1>Список пользователей:</h1>");
                if (users.isEmpty()) {
                    writer.write("<p>Пользователи не найдены.</p>");
                } else {
                    writer.write("<ul>");
                    for (UserDto userDto : users) {
                        writer.write(String.format(
                                "<li><a href='/posts?userId=%d'>%s</a></li>",
                                userDto.getId(), userDto.getUsername()
                        ));
                    }
                    writer.write("</ul>");
                }
            } catch (DaoException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error accessing user data");
            }
        }
    }
}
