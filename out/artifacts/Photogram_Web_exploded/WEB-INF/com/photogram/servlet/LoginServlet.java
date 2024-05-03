package com.photogram.servlet;

import com.photogram.dto.UserDto;
import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.UserService;
import com.photogram.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ServletPhotogramException("Error while processing request", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        userService.login(req.getParameter("email"), req.getParameter("password"))
                .ifPresentOrElse(
                        user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(req, resp));

    }

    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect("/login?error" + req.getParameter("email"));
        } catch (IOException e) {
            throw new ServletPhotogramException("Login failed", e);
        }
    }

    private void onLoginSuccess(UserDto user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", user);
        try {
            resp.sendRedirect("/users");
        } catch (IOException e) {
            throw new ServletPhotogramException("Error sending redirect to login page", e);
        }
    }
}
