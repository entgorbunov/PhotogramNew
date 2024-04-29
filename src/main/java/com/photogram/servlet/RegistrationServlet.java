package com.photogram.servlet;import com.photogram.exceptions.ValidationException;import com.photogram.dto.CreateUserDto;import com.photogram.service.ImageService;import com.photogram.service.UserService;import com.photogram.util.JspHelper;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.MultipartConfig;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.HttpServlet;import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletResponse;import jakarta.servlet.http.Part;import java.io.IOException;import java.nio.file.Paths;import java.util.List;@MultipartConfig(fileSizeThreshold = 1024 * 1024)@WebServlet(value = "/registration")public class RegistrationServlet extends HttpServlet {    public static final String USERS_ENT_DESKTOP_PICTURES = "/Users/ent/Desktop/Pictures";    private final UserService userService = UserService.getInstance();    private final ImageService imageService = ImageService.getINSTANCE();    @Override    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        req.setAttribute("roles", List.of("USER", "ADMIN"));        req.setAttribute("genders", List.of("MALE", "FEMALE"));        req.getRequestDispatcher(JspHelper.getPath("registration")).forward(req, resp);    }    @Override    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        Part imagePart = req.getPart("image");        String submittedFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();        String imageFileName = submittedFileName;        String imagePath = USERS_ENT_DESKTOP_PICTURES + "/" + imageFileName;        imageService.upload(imagePath, imagePart.getInputStream());        CreateUserDto userDto = CreateUserDto.builder()                .name(req.getParameter("name"))                .password(req.getParameter("password"))                .image(req.getPart("image"))                .email(req.getParameter("email"))                .birthday(req.getParameter("birthday"))                .role(req.getParameter("role"))                .gender(req.getParameter("gender"))                .build();        try {            userService.create(userDto);            resp.sendRedirect("/login");        } catch (ValidationException e) {            req.setAttribute("errors", e.getErrors());            doGet(req, resp);        }    }}