package com.photogram.filter;

import com.photogram.dto.userDto.UserDtoFromWeb;
import jakarta.servlet.*;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.photogram.util.UrlPath.*;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {


    public static final Set<String> PUBLIC_PATH = Set.of(LOGIN, REGISTRATION, IMAGES);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (isPublicPath(uri) || isUserLoggedIn(request)){
            chain.doFilter(request, response);
        } else {
            String previousPage = ((HttpServletRequest) request).getHeader("referer");
            ((HttpServletResponse) response).sendRedirect(
                    previousPage != null
                            ? previousPage:
                            LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest request) {
        UserDtoFromWeb user = (UserDtoFromWeb) ((HttpServletRequest) request).getSession().getAttribute("user");
        return user != null;
    }


    private boolean isPublicPath(String uri) {
        return AuthorizationFilter.PUBLIC_PATH.stream()
                .anyMatch(uri::startsWith);
    }
}
