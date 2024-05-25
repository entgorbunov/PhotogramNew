package com.photogram.servlet;

import com.photogram.dto.postDto.PostDtoFromWeb;
import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.postService.PostServiceForWeb;
import com.photogram.util.Constants;
import com.photogram.util.JspHelper;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(UrlPath.ADD_POST)
@MultipartConfig(location = UrlPath.POSTS_PICTURES, fileSizeThreshold = 1024 * 1024)
public class PostServlet extends HttpServlet {
    private static final PostServiceForWeb POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE = PostServiceForWeb.getPostServiceForWebAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher(JspHelper.getPath(UrlPath.ADD_POST)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute(Constants.ACTIVE_USER_ID) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.SESSION_IS_INVALID_OR_EXPIRED);
            return;
        }

        try {
            Long userId = (Long) session.getAttribute(Constants.ACTIVE_USER_ID);
            String content = req.getParameter(Constants.CONTENT);
            Part image = req.getPart(Constants.IMAGE);

            if ((content == null || content.trim().isEmpty()) && (image == null || image.getSize() == 0)) {
                req.setAttribute("errorMessage", "Cannot create a post without image and text.");
                req.getRequestDispatcher(JspHelper.getPath(UrlPath.MAIN_PAGE)).forward(req, resp);
                return;
            }

            LocalDateTime localDateTime = LocalDateTime.now();

            PostDtoFromWeb postDtoFromWeb = PostDtoFromWeb.builder()
                    .userId(userId)
                    .text(content)
                    .image(image)
                    .postTime(localDateTime)
                    .build();

            POST_SERVICE_FOR_WEB_ATOMIC_REFERENCE.create(postDtoFromWeb);
            resp.sendRedirect(UrlPath.MAIN_PAGE);

        } catch (NumberFormatException | ServletException e) {
            throw new ServletPhotogramException("Invalid input", e);
        }
    }
}
