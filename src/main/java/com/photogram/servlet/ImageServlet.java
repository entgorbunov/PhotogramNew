package com.photogram.servlet;

import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.ImageService;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(UrlPath.IMAGES + "/*")
public class ImageServlet extends HttpServlet {

    private final ImageService imageService = ImageService.getINSTANCE();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String imagePath = extractImagePath(req);
        serveImage(imagePath, resp);
    }

    private String extractImagePath(HttpServletRequest req) {
        return req.getRequestURI().replace("/images", "");
    }

    private void serveImage(String imagePath, HttpServletResponse resp) {
        imageService.get(imagePath).ifPresentOrElse(
                image -> sendImageResponse(image, resp),
                () -> sendNotFoundResponse(resp)
        );
    }

    private void sendImageResponse(InputStream image, HttpServletResponse resp) {
        resp.setContentType("application/octet-stream");
        try (InputStream localImage = image) {
            writeImage(localImage, resp.getOutputStream());
        } catch (IOException e) {
            throw new ServletPhotogramException("Error while writing image", e);
        }
    }

    private void writeImage(InputStream image, ServletOutputStream outputStream) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        try {
            while ((bytesRead = image.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new ServletPhotogramException("Error while writing image", e);
        }
    }


    private static void sendNotFoundResponse(HttpServletResponse resp) {
        try {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            throw new ServletPhotogramException("Could not send image response", e);
        }
    }
}
