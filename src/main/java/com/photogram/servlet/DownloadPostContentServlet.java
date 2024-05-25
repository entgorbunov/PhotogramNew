package com.photogram.servlet;

import com.photogram.dao.PostDao;
import com.photogram.entity.Post;
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

@WebServlet(UrlPath.DOWNLOAD_POST_IMAGE)
public class DownloadPostContentServlet extends HttpServlet {
    private static final PostDao POST_DAO_ATOMIC_REFERENCE = PostDao.getPostDaoAtomicReference();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String imagePath;
            imagePath = getUserPostPath(req);
            sendPostImage(resp, imagePath);
        } catch (Exception e) {
            throw new ServletPhotogramException("Error downloading image", e);
        }
    }

    private String getUserPostPath(HttpServletRequest req) {
        Long postId = Long.parseLong(req.getParameter(Constants.POST_ID));
        Optional<Post> optionalPost = POST_DAO_ATOMIC_REFERENCE.findById(postId);
        if (optionalPost.isPresent() && optionalPost.get().getImageUrl() != null) {
            return optionalPost.get().getImageUrl();
        } else {
            throw new ServletPhotogramException("User not found or no image available");
        }
    }

    private void sendPostImage(HttpServletResponse resp, String imagePath) {
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
