package com.photogram.servlet;

import com.photogram.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    private final transient PostService postService = PostService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        var userId = Long.valueOf(req.getParameter("userId"));

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.write("<h1>Опубликованные посты</h1>");
            writer.write("<ul>");
            postService.findAllByUserId(userId).forEach(postDto -> writer.write("""
                    <li>
                        %s
                    </li>
                    """.formatted(postDto.getCaption())));
            writer.write("</ul>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
