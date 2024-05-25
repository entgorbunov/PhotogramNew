package com.photogram.servlet;

import com.photogram.dao.UserDao;
import com.photogram.entity.User;
import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.util.Constants;
import com.photogram.util.UrlPath;
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

@WebServlet(UrlPath.DOWNLOAD_USER_IMAGE)
public class DownloadUserContentServlet extends HttpServlet {

    private static final UserDao USER_DAO_ATOMIC_REFERENCE = UserDao.getUserDaoAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String imagePath;
            imagePath = getUserImagePath(req);
            sendImage(resp, imagePath);
        } catch (Exception e) {
            throw new ServletPhotogramException("Error downloading image", e);
        }
    }

    private String getUserImagePath(HttpServletRequest req) {
        Long userId = Long.parseLong(req.getParameter(Constants.USER_ID));
        Optional<User> user = USER_DAO_ATOMIC_REFERENCE.findById(userId);
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
