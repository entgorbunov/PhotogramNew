package com.photogram.servlet;

import com.photogram.dto.SubscriptionDto;
import com.photogram.exceptions.ServletPhotogramException;
import com.photogram.service.SubscriptionService;
import com.photogram.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/addSubscription")
public class SubscriptionAddServlet extends HttpServlet {

    private static final SubscriptionService SUBSCRIPTION_SERVICE = SubscriptionService.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute(Constants.ACTIVE_USER_ID) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.SESSION_IS_INVALID_OR_EXPIRED);
            return;
        }

        try {
            Long userId = Long.parseLong(req.getParameter(Constants.USER_ID));
            Long followerId = (Long) session.getAttribute(Constants.ACTIVE_USER_ID);




            SubscriptionDto subscriptionDto = SubscriptionDto.builder()
                    .userId(userId)
                    .followerId(followerId)
                    .subscriptionDate(LocalDate.now())
                    .build();

            SUBSCRIPTION_SERVICE.create(subscriptionDto);

            req.getSession().setAttribute("subscriptionMessage", "Subscription added successfully!");

            resp.sendRedirect(req.getHeader("Referer"));

        } catch (NumberFormatException e) {
            throw new ServletPhotogramException("Invalid input", e);
        }
    }
}
