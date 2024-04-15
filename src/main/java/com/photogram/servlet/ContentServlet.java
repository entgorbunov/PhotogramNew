package com.photogram.servlet;

import com.photogram.dto.UserDto;
import com.photogram.service.UserService;
import com.photogram.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/content")
public class ContentServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userDtos = userService.findAll();
        req.setAttribute("users", userDtos);
        req.setAttribute("usersMap", userDtos.stream().collect(Collectors.toMap(UserDto::getId,
                UserDto::getUsername)));


        req.getRequestDispatcher(JspHelper.getPath("content")).forward(req, resp);
    }
}
