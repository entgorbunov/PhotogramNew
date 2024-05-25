package com.photogram.servlet;

import com.photogram.service.postService.PostServiceForDataBase;
import com.photogram.util.Constants;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet(UrlPath.DELETE_POST)
public class DeletePostServlet extends HttpServlet {

    private static final PostServiceForDataBase POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE = PostServiceForDataBase.getPostServiceForDataBaseAtomicReference();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constants.ACTIVE_USER) == null) {
            resp.sendRedirect(UrlPath.LOGIN);
            return;
        }
        Long postId = Long.parseLong(req.getParameter(Constants.POST_ID));
        POST_SERVICE_FOR_DATA_BASE_ATOMIC_REFERENCE.delete(postId);

        resp.sendRedirect(UrlPath.MAIN_PAGE);

    }
}
