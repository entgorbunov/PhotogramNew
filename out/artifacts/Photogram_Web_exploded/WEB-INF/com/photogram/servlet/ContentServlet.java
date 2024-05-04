package com.photogram.servlet;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.service.UserServiceForWeb;
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

    private final UserServiceForWeb userServiceForWeb = UserServiceForWeb.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserDtoFromWeb> userDtoFromWebs = userServiceForWeb.findAll();

        setRequestAttributes(req, userDtoFromWebs);
        setSessionAttributes(req, userDtoFromWebs);

        forwardToJsp(req, resp);
    }

    private void setRequestAttributes(HttpServletRequest req, List<UserDtoFromWeb> userDtoFromWebs) {
        req.setAttribute("users", userDtoFromWebs);
    }

    private void setSessionAttributes(HttpServletRequest req, List<UserDtoFromWeb> userDtoFromWebs) {
        Map<String , String> usersMap = userDtoFromWebs.stream()
                .collect(Collectors.toMap(UserDtoFromWeb::getEmail, UserDtoFromWeb::getName));
        req.getSession().setAttribute("usersMap", usersMap);
    }

    private void forwardToJsp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("content")).forward(req, resp);
    }
}
