package com.photogram.servlet;

import com.photogram.dataSource.ConnectionManager;
import com.photogram.dto.postDto.PostDtoFromDataBase;
import com.photogram.dto.userDto.UserDtoFromDataBase;
import com.photogram.service.SubscriptionService;
import com.photogram.service.postService.PostServiceForDataBase;
import com.photogram.service.userService.UserServiceForDataBase;
import com.photogram.util.Constants;
import com.photogram.util.JspHelper;
import com.photogram.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(UrlPath.MAIN_PAGE)
public class MainPageServlet extends HttpServlet {

    private static final UserServiceForDataBase USER_SERVICE_FOR_DATA_BASE = UserServiceForDataBase.getUserServiceForDataBaseAtomicReference();
    private static final PostServiceForDataBase POST_SERVICE_FOR_DATA_BASE = PostServiceForDataBase.getPostServiceForDataBaseAtomicReference();
    private static final SubscriptionService SUBSCRIPTION_SERVICE = SubscriptionService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Long activeUserId = (Long) session.getAttribute(Constants.ACTIVE_USER_ID);
            if (activeUserId != null) {

                String userIdParam = req.getParameter("userId");
                Long userId = userIdParam != null ? Long.parseLong(userIdParam) : activeUserId;

                UserDtoFromDataBase userDtoFromDataBase = USER_SERVICE_FOR_DATA_BASE.findByUserId(userId);

                if (userDtoFromDataBase != null) {
                    req.setAttribute("activeUserId", activeUserId);
                    req.setAttribute(Constants.USER, userDtoFromDataBase);

                    if (userId.equals(activeUserId)) {
                        List<UserDtoFromDataBase> recommendedUsers = USER_SERVICE_FOR_DATA_BASE.getRecommendedUsers(userId);
                        req.setAttribute(Constants.RECOMMENDED_USERS, recommendedUsers);
                    }

                    List<UserDtoFromDataBase> subscriptionUsers = USER_SERVICE_FOR_DATA_BASE.getSubscriptions(userId);
                    req.setAttribute("subscriptionUsers", subscriptionUsers);

                    List<PostDtoFromDataBase> postDtoFromDataBaseList = POST_SERVICE_FOR_DATA_BASE.findAllActivePostsByUserId(userId);
                    req.setAttribute(Constants.POSTS, postDtoFromDataBaseList);

                    req.getRequestDispatcher(JspHelper.getPath(UrlPath.MAIN_PAGE)).forward(req, resp);
                } else {
                    resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
                }
            } else {
                resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + UrlPath.LOGIN);
        }
    }

    @Override
    public void destroy() {
        ConnectionManager.close();
    }
}


