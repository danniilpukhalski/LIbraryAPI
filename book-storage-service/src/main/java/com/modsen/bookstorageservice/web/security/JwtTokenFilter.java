package com.modsen.bookstorageservice.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.bookstorageservice.domain.exception.ExceptionBody;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenFilter extends GenericFilterBean {
    JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);

        }
        try {

            if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
                try {
                    Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (ResourceNotFoundException ignored) {

                }
            }
        } catch (Exception e) {
            exceptionHandler((HttpServletResponse) servletResponse, e);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void exceptionHandler(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionBody exceptionBody;
        if (exception instanceof ExpiredJwtException) {
            exceptionBody = new ExceptionBody("Expired JWT");
        } else {
            exceptionBody = new ExceptionBody("Invalid JWT");
        }
        response.getWriter().write(objectMapper.writeValueAsString(exceptionBody));
    }
}