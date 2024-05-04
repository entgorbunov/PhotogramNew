package com.photogram.servlet;

import com.photogram.service.ImageService;
import com.photogram.service.UserServiceForWeb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/mainPage")
public class MainPageServlet extends HttpServlet {
    public static final UserServiceForWeb USER_SERVICE_FOR_WEB = UserServiceForWeb.getInstance();
    public static final ImageService imageService = ImageService.getINSTANCE();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
