package com.photogram.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getHeader("user-agent");
        Collection<String> headerNames = resp.getHeaderNames();
        while (headerNames.iterator().hasNext()) {
            String headerName = headerNames.iterator().next();
            System.out.println(headerName);
        }
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            out.write("Hello from FirstServlet");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
