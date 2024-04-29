package com.photogram.servlet;

import com.photogram.Exceptions.ServletPhotogramException;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Long userId = Long.valueOf(req.getParameter("userId"));
        req.setAttribute("posts", postService.findAllByUserId(userId));
        try {
            req.getRequestDispatcher(JspHelper.getPath("posts")).forward(req, resp);
        } catch (ServletException e) {
            throw new ServletPhotogramException("Error while processing request", e);
        }
    }
}
