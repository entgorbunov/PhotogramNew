package com.photogram.servlet;import com.photogram.dto.userDto.UserDtoFromWeb;import com.photogram.entity.Gender;import com.photogram.entity.Role;import com.photogram.exceptions.ValidationException;import com.photogram.service.UserServiceForWeb;import com.photogram.util.JspHelper;import com.photogram.util.UrlPath;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.MultipartConfig;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.HttpServlet;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import java.io.IOException;@MultipartConfig(location = "/Users/ent/Desktop/Pictures/users/", fileSizeThreshold = 1024 * 1024)@WebServlet(value = UrlPath.REGISTRATION, name = "RegistrationServlet")public class RegistrationServlet extends HttpServlet {    private final UserServiceForWeb userServiceForWeb = UserServiceForWeb.getInstance();    @Override    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        req.setAttribute("roles", Role.values());        req.setAttribute("genders", Gender.values());        req.getRequestDispatcher(JspHelper.getPath("registration")).forward(req, resp);    }    @Override    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        UserDtoFromWeb userDtoFromWeb = UserDtoFromWeb.builder()                .name(req.getParameter("name"))                .password(req.getParameter("password"))                .image(req.getPart("image"))                .email(req.getParameter("email"))                .birthday(req.getParameter("birthday"))                .role(req.getParameter("role"))                .gender(req.getParameter("gender"))                .build();        try {            userServiceForWeb.create(userDtoFromWeb);            resp.sendRedirect("/login");        } catch (ValidationException exception) {            req.setAttribute("error", exception.getErrors());            doGet(req, resp);        }    }}