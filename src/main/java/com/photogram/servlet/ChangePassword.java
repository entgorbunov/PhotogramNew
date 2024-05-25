package com.photogram.servlet;

import com.photogram.service.userService.UserServiceForWeb;
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

@WebServlet(UrlPath.CHANGE_PASSWORD)
public class ChangePassword extends HttpServlet {

    private static final UserServiceForWeb USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE = UserServiceForWeb.getUserServiceForWebAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER_ID) == null) {
            resp.sendRedirect(UrlPath.LOGIN);
            return;
        }

        req.getRequestDispatcher(JspHelper.getPath(UrlPath.CHANGE_PASSWORD)).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute(Constants.ACTIVE_USER_ID);
        String oldPassword = req.getParameter(Constants.OLD_PASSWORD);
        String newPassword = req.getParameter(Constants.NEW_PASSWORD);
        String confirmNewPassword = req.getParameter(Constants.CONFIRM_NEW_PASSWORD);
        if (!newPassword.equals(confirmNewPassword)) {
            req.setAttribute(Constants.ERROR_MESSAGE, "New passwords do not match!");
            req.getRequestDispatcher(JspHelper.getPath(UrlPath.CHANGE_PASSWORD)).forward(req, resp);
            return;
        }

        try {
            USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.changePassword(userId, oldPassword, newPassword);
            req.setAttribute(Constants.MESSAGE, "Password successfully updated!");
        } catch (Exception e) {
            req.setAttribute(Constants.ERROR_MESSAGE, e.getMessage());
        }
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.CHANGE_PASSWORD)).forward(req, resp);
    }

}

