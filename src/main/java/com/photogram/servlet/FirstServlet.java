//package com.photogram.servlet;
//
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.stream.Stream;
//
//@WebServlet("/first")
//public class FirstServlet extends HttpServlet {
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        var paranValue = req.getParameter("param");
//        var parameterMap = req.getParameterMap();
//        System.out.println();
//        resp.setContentType("text/html; charset=UTF-8");
//        resp.setHeader("token", "12345");
//        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        try (var writer = resp.getWriter()) {
//            writer.write("<h1>Привет от первого сервлета!</h2>");
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try (var reader = req.getReader()) {
//            try (var lines = reader.lines()) {
//                lines.forEach(System.out::println);
//            }
//        }
//    }
//
//
//    @Override
//    public void destroy() {
//        super.destroy();
//    }
//}
