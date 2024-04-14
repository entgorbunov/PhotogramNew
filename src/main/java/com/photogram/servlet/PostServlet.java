package com.photogram.servlet;

import com.photogram.daoException.ServletException;
import com.photogram.dto.PostDto;
import com.photogram.service.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.util.List;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    private final transient PostService postService = PostService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userIdParam = req.getParameter("userId");
        Long userId;
        try {
            userId = Long.valueOf(userIdParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.write("<h1>Опубликованные посты:</h1>");
            List<PostDto> posts = postService.findAllPostsByUserId(userId);
            if (posts.isEmpty()) {
                writer.write("<p>Посты не найдены для пользователя с ID: " + userId + ".</p>");
            } else {
                writer.write("<ul>");
                posts.forEach(postDto -> writer.write(String.format(
                        "<li>%s</li>",
                        postDto.getCaption()
                )));
                writer.write("</ul>");
            }
        } catch (IOException e) {
            throw new ServletException("Error writing response", e);
        }
    }
}
