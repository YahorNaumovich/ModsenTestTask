package com.example.libraryservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

@Component
@RequiredArgsConstructor

//This class is used to check if the incoming request has a valid jwt and authenticate the user
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest =(HttpServletRequest) servletRequest;
        String requestUrl = String.valueOf(httpRequest.getRequestURL());
        if (!requestUrl.contains("/swagger") && !requestUrl.contains("api-docs")) {
            String token = jwtTokenProvider.resolveTokenFromRequest((HttpServletRequest) servletRequest);
            if (token == null) {
                HttpServletResponse http = (HttpServletResponse) servletResponse;
                http.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No authorization token detected");
            } else if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
