package com.photogram.servlet;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.service.userService.UserServiceForWeb;
import com.photogram.util.Constants;
import com.photogram.util.JspHelper;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


@WebServlet(UrlPath.CHANGE_IMAGE)
@MultipartConfig(location = UrlPath.USERS_PICTURES, fileSizeThreshold = 1024 * 1024)
public class ChangeImageServlet extends HttpServlet {

    public static final String IMAGE_SUCCESSFULLY_UPLOADED = "Image successfully uploaded!";
    public static final String NO_FILE_TO_UPLOAD = "No file to upload!";
    private static final UserServiceForWeb USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE = UserServiceForWeb.getUserServiceForWebAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(UrlPath.LOGIN);
            return;
        }
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.CHANGE_IMAGE)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER_ID) == null) {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            return;
        }
        Long userId = (Long) session.getAttribute(Constants.ACTIVE_USER_ID);
        Part imagePart = req.getPart(Constants.IMAGE);
        if (imagePart != null && imagePart.getSize() > 0) {
            UserDtoFromWeb dtoFromWeb = UserDtoFromWeb.builder()
                    .id(userId)
                    .image(imagePart)
                    .build();

            UserDtoFromWeb userDtoFromWeb = USER_SERVICE_FOR_WEB_ATOMIC_REFERENCE.updateWithImage(dtoFromWeb);
            session.setAttribute(Constants.ACTIVE_USER, userDtoFromWeb);
            req.setAttribute(Constants.MESSAGE, IMAGE_SUCCESSFULLY_UPLOADED);
        } else {
            req.setAttribute(Constants.MESSAGE, NO_FILE_TO_UPLOAD);
        }
        resp.sendRedirect(req.getContextPath() + UrlPath.CHANGE_IMAGE);
    }

}
