package com.wonder4.financeportfoliobackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("Request Intercepted: [{}] {}", request.getMethod(), request.getRequestURI());

        // Example Logic (Commented out):
        // String token = request.getHeader("Authorization");
        // if (token == null || !token.startsWith("Bearer ")) {
        //     response.setStatus(401);
        //     response.getWriter().write("{\"code\":401, \"message\":\"Unauthorized\"}");
        //     return false;
        // }

        return true;
    }
}
