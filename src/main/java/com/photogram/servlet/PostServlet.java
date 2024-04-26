package com.photogram.servlet;

import com.photogram.daoException.ServletPhotogramException;
import com.photogram.service.PostService;
import com.photogram.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    private final PostService postService = PostService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Long userId = Long.valueOf(req.getParameter("userId"));
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write("<h1>Опубликованные посты</h1>");
            printWriter.write("<ul>");
            postService.findAllByUserId(userId).forEach(postDto -> printWriter.write("""
                    <li>
                        %s
                    </li>
                    """.formatted(postDto.getCaption())));
            printWriter.write("<ul>");
        }


        req.setAttribute("posts", postService.findByUserId(userId));


        try {
            req.getRequestDispatcher(JspHelper.getPath("posts")).forward(req, resp);
        } catch (ServletException e) {
            throw new ServletPhotogramException(e);
        }
    }
}
