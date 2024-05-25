package com.photogram.servlet;

import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.userService.UserServiceForDataBase;
import com.photogram.util.Constants;
import com.photogram.util.JspHelper;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(UrlPath.LOGIN)
public class LoginServlet extends HttpServlet {

    private static final UserServiceForDataBase USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE = UserServiceForDataBase.getUserServiceForDataBaseAtomicReference();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            req.getRequestDispatcher(JspHelper.getPath(UrlPath.LOGIN)).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ServletPhotogramException("Error while processing request", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        USER_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.login(req.getParameter(Constants.EMAIL), req.getParameter(Constants.PASSWORD))
                .ifPresentOrElse(
                        user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(req, resp));

    }

    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect("/login?error=true&email=" + req.getParameter(Constants.EMAIL));
        } catch (IOException e) {
            throw new ServletPhotogramException(Constants.LOGIN_FAILED, e);
        }
    }

    private void onLoginSuccess(UserDtoFromDataBase user, HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        Long id = user.getId();
        session.setAttribute(Constants.ACTIVE_USER_ID, id);
        session.setAttribute(Constants.ACTIVE_USER, user);
        req.setAttribute(Constants.USER, user);
        try {
            resp.sendRedirect(UrlPath.MAIN_PAGE);
        } catch (IOException e) {
            req.setAttribute(Constants.ERROR_MESSAGE, "Unable to redirect. Please try again later.");
            try {
                req.getRequestDispatcher(JspHelper.getPath(UrlPath.LOGIN)).forward(req, resp);
            } catch (ServletException | IOException ex) {
                throw new ServletPhotogramException("Error handling redirect failure", ex);
            }
        }

    }
}
