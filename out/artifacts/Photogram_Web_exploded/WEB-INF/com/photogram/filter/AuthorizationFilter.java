package com.photogram.filter;

import com.photogram.dto.userDto.UserDtoFromWeb;
import com.photogram.entity.Role;
import com.photogram.util.Constants;
import com.photogram.util.UrlPath;
import jakarta.servlet.*;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.photogram.util.UrlPath.LOGIN;
import static com.photogram.util.UrlPath.REGISTRATION;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {


    public static final Set<String> PUBLIC_PATH = Set.of(LOGIN, REGISTRATION);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();

        System.out.println("AuthorizationFilter: Processing request for URI: " + uri);

        if (isPublicPath(uri)) {
            System.out.println("AuthorizationFilter: Public path, allowing access.");
            chain.doFilter(request, response);
        } else {
            if (isUserLoggedIn(request)) {
                UserDtoFromWeb user = (UserDtoFromWeb) httpRequest.getSession().getAttribute(Constants.ACTIVE_USER);
                System.out.println("AuthorizationFilter: User is logged in with role: " + user.getRole());

                if (uri.startsWith(UrlPath.ADMIN_PAGE) && user.getRole() != Role.ADMIN) {
                    System.out.println("AuthorizationFilter: User is not admin, redirecting to main page.");
                    httpResponse.sendRedirect(UrlPath.MAIN_PAGE);
                } else {
                    System.out.println("AuthorizationFilter: User is admin or accessing non-admin page, allowing access.");
                    chain.doFilter(request, response);
                }
            } else {
                System.out.println("AuthorizationFilter: User is not logged in, redirecting to login page.");
                String previousPage = httpRequest.getHeader("referer");
                httpResponse.sendRedirect(previousPage != null ? previousPage : UrlPath.LOGIN);
            }
        }
    }

    private boolean isUserLoggedIn(ServletRequest request) {
        UserDtoFromWeb user = (UserDtoFromWeb) ((HttpServletRequest) request).getSession().getAttribute(Constants.ACTIVE_USER);
        return user != null;
    }


    private boolean isPublicPath(String uri) {
        return AuthorizationFilter.PUBLIC_PATH.stream()
                .anyMatch(uri::startsWith);
    }
}
