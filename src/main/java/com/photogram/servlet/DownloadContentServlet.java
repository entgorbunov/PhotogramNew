package com.photogram.servlet;

import com.photogram.dao.UserDao;
import com.photogram.entity.User;
import com.photogram.exceptions.ServletPhotogramException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@WebServlet("/downloadImage")
public class DownloadContentServlet extends HttpServlet {

    private final UserDao userDao = UserDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String imagePath = null;
            imagePath = getUserImage(req);
            sendImage(resp, imagePath);
        } catch (Exception e) {
            throw new ServletPhotogramException("Error downloading image", e);
        }
    }

    private String getUserImage(HttpServletRequest req) {
        Long userId = Long.parseLong(req.getParameter("userId"));
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent() && user.get().getImage() != null) {
            return user.get().getImage();
        } else {
            throw new ServletPhotogramException("User not found or no image available");
        }
    }

    private void sendImage(HttpServletResponse resp, String imagePath) {
        resp.setContentType("image/jpeg");
        Path path = Paths.get(imagePath);
        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(path, out);
            out.flush();
        } catch (IOException e) {
            throw new ServletPhotogramException("Error sending image", e);
        }
    }
}
