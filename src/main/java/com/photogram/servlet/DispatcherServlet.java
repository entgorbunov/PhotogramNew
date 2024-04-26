package com.photogram.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dispatcher")
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher("/users").include(req, resp);
//        PrintWriter writer = resp.getWriter();
//        writer.write("Hello 2");
//
////        req.setAttribute("1", "234");
//        System.out.println();

        resp.sendRedirect("/users");
    }
}
