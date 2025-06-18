package com.home.homepage.filter;

import com.home.homepage.entity.User;
import com.home.homepage.utils.UserContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jachin
 * @since 25-06-17 14:43
 */
@Component
public class AuthInterceptor implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 校验白名单
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (uri.isEmpty() || uri.equals("/")
                || uri.startsWith("/login")
                || uri.startsWith("/register")
                || uri.startsWith("/auth")
                || uri.startsWith("/static")
                || uri.startsWith("/home")) {
            chain.doFilter(request, response);
            System.out.println("white list");
            return;
        }


        // Check if the user is authenticated or not.
        String sessionId = ((HttpServletRequest) request).getSession().getId();
        User user = UserContext.getUser(sessionId);
        if (user == null) {
            ((HttpServletResponse) response).sendRedirect("/login");
            System.out.println("get by sessionId....redirect to login");
            return;
        }

        System.out.println("already login....");
        // If authenticated, allow the request to proceed.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
