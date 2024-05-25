package com.photogram.servlet;

import com.photogram.dao.UserDao;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.entity.Role;
import com.photogram.entity.User;
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
import java.util.List;

@WebServlet(UrlPath.ADMIN_PAGE)
public class AdminServlet extends HttpServlet {
    private static final UserDao USER_DAO = UserDao.getUserDaoAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            return;
        }
        UserDtoFromDataBase user = (UserDtoFromDataBase) session.getAttribute(Constants.ACTIVE_USER);
        if (user.getRole() != Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            return;
        }
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.ADMIN_PAGE)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            return;
        }

        UserDtoFromDataBase user = (UserDtoFromDataBase) session.getAttribute(Constants.ACTIVE_USER);
        if (user.getRole() != Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            return;
        }

        String action = req.getParameter("action");

        switch (action) {
            case "delete" -> deleteUser(req, resp);
            case "findAll" -> findAllUsers(resp);
            case "assignRole" -> assignRole(req, resp);
            case "restore" -> restoreUser(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        USER_DAO.delete(id);
        resp.getWriter().write("User deleted successfully");
    }

    private void findAllUsers(HttpServletResponse resp) throws IOException {
        List<User> allUsers = USER_DAO.findAll();
        resp.getWriter().write(allUsers.toString());
    }

    private void assignRole(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Long id = Long.parseLong(req.getParameter("id"));
        Role newRole = Role.valueOf(req.getParameter("role"));
        User user = USER_DAO.findById(id).orElseThrow(() -> new ServletException("User with id " + id + " not found"));
        if (user != null) {
            user.setRole(newRole);
            USER_DAO.update(user);
            resp.getWriter().write("Role assigned successfully");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

    private void restoreUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        USER_DAO.restore(id);
        resp.getWriter().write("User restored successfully");
    }
}