package com.photogram.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/cookies")
public class CookieServlet extends HttpServlet {

    private static final String UNIQUE_ID = "userId";
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ensureUserCookieExists(req, resp);
        sendResponse(resp);
    }

    private void ensureUserCookieExists(HttpServletRequest req, HttpServletResponse resp) {
        if (isNewUser(req)) {
            createNewUserCookie(resp);
        }
    }

    private boolean isNewUser(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        return cookies == null || Arrays.stream(cookies)
                .noneMatch(cookie -> UNIQUE_ID.equals(cookie.getName()));
    }

    private void createNewUserCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie(UNIQUE_ID, Integer.toString(counter.incrementAndGet()));
        cookie.setPath("/cookies");
        cookie.setMaxAge(15 * 60); // 15 minutes
        resp.addCookie(cookie);
    }

    private void sendResponse(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        try (PrintWriter writer = resp.getWriter()) {
            writer.print("<html><body>");
            writer.print("User ID set to: " + counter.get());
            writer.print("</body></html>");
        }
    }
}
