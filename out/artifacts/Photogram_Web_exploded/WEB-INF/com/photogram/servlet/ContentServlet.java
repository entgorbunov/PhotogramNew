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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/content")
public class ContentServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserDto> userDtos = userService.findAll();

        setRequestAttributes(req, userDtos);
        setSessionAttributes(req, userDtos);

        forwardToJsp(req, resp);
    }

    private void setRequestAttributes(HttpServletRequest req, List<UserDto> userDtos) {
        req.setAttribute("users", userDtos);
    }

    private void setSessionAttributes(HttpServletRequest req, List<UserDto> userDtos) {
        Map<String , String> usersMap = userDtos.stream()
                .collect(Collectors.toMap(UserDto::getEmail, UserDto::getName));
        req.getSession().setAttribute("usersMap", usersMap);
    }

    private void forwardToJsp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("content")).forward(req, resp);
    }
}
