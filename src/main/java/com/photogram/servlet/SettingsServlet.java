package com.photogram.servlet;

import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Gender;
import com.photogram.service.userService.UserServiceForWeb;
import com.photogram.util.Constants;
import com.photogram.util.JspHelper;
import com.photogram.util.LocalDateTimeFormatter;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
@MultipartConfig(location = UrlPath.USERS_PICTURES, fileSizeThreshold = 1024 * 1024)
@WebServlet(UrlPath.SETTINGS)
public class SettingsServlet extends HttpServlet {

    private static final UserServiceForWeb USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE = UserServiceForWeb.getUserServiceForWebAtomicReference();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(UrlPath.LOGIN);
            return;
        }

        req.getRequestDispatcher(JspHelper.getPath(Constants.SETTINGS)).forward(req, resp);
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(UrlPath.LOGIN);
            return;
        }

        UserDtoFromDataBase userDto = (UserDtoFromDataBase) session.getAttribute(Constants.ACTIVE_USER);

        String email = req.getParameter(Constants.EMAIL);
        String username = req.getParameter(Constants.USERNAME);
        String password = req.getParameter(Constants.PASSWORD);
        String name = req.getParameter(Constants.NAME);
        String birthdayStr = req.getParameter(Constants.BIRTHDAY);
        String genderStr = req.getParameter(Constants.GENDER);
        String bio = req.getParameter(Constants.BIO);

        LocalDateTime birthday = null;
        if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
            birthday = LocalDateTimeFormatter.format(birthdayStr).atStartOfDay();
        }

        Gender gender = null;
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            Optional<Gender> optionalGender = Gender.find(genderStr);
            if (optionalGender.isPresent()) {
                gender = optionalGender.get();
            } else {
                throw new ServletException("Invalid gender value provided");
            }
        }

        UserDtoFromWeb.UserDtoFromWebBuilder filterBuilder = UserDtoFromWeb.builder()
                .id(userDto.getId());

        if (email != null && !email.trim().isEmpty()) filterBuilder.email(email);
        if (username != null && !username.trim().isEmpty()) filterBuilder.username(username);
        if (password != null && !password.trim().isEmpty()) filterBuilder.password(password);
        if (name != null && !name.trim().isEmpty()) filterBuilder.name(name);
        if (birthday != null) filterBuilder.birthday(LocalDate.from(birthday));
        if (gender != null) filterBuilder.gender(gender);
        if (bio != null && !bio.trim().isEmpty()) filterBuilder.bio(bio);

        UserDtoFromWeb updatedUser = USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.update(filterBuilder.build());
        session.setAttribute(Constants.USER, updatedUser);
        resp.sendRedirect(UrlPath.SETTINGS);
    }





}
