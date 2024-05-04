package com.photogram.servlet;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.service.UserServiceForDataBase;
import com.photogram.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {


    private final UserServiceForDataBase userServiceForDataBase = UserServiceForDataBase.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserDtoFromDataBase> serviceForDataBaseAll = userServiceForDataBase.findAll();
        req.setAttribute("users", serviceForDataBaseAll);
        req.getRequestDispatcher(JspHelper.getPath("users")).forward(req, resp);


    }

    @Override
    public void destroy() {
        ConnectionManager.close();
    }
}
