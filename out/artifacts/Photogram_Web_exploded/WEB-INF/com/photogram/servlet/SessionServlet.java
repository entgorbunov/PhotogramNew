package com.photogram.servlet;

import com.photogram.dto.UserDto;
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

//        req.getServletContext()
        var session = req.getSession();

        var user = (UserDto) session.getAttribute(USER);
        if (user == null) {
            user = UserDto.builder()
                    .id(1L)
                    .bio("пиу пиу")
                    .build();
            session.setAttribute(USER, user);
        }


    }
}