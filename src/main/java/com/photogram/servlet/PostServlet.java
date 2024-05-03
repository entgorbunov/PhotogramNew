package com.photogram.servlet;

import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.PostService;
import com.photogram.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    private final PostService postService = PostService.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            String userIdParam = req.getParameter("userId");
            Long userId = Long.valueOf(userIdParam);

            req.setAttribute("posts", postService.findAllByUserId(userId));

            String jspPath = JspHelper.getPath("posts");
            req.getRequestDispatcher(jspPath).forward(req, resp);
        } catch (ServletException | NumberFormatException e) {
            throw new ServletPhotogramException("Error while processing request", e);
        }
    }

}
