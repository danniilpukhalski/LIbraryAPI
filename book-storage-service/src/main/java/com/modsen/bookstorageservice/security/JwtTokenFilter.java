package com.modsen.bookstorageservice.security;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
            log.info("Extracted Bearer token from Authorization header");
        }

        try {
            if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
                log.info("Token is valid. Proceeding to authenticate...");

                try {
                    Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("Authentication successful for token.");
                    }
                } catch (ResourceNotFoundException ignored) {
                    log.warn("Authentication failed, user not found for token.");
                }
            } else {
                log.warn("Invalid or expired token.");
            }
        } catch (Exception e) {
            log.error("Error occurred while processing the token: {}", e.getMessage(), e);
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
            log.error("JWT has expired.");
        } else {
            exceptionBody = new ExceptionBody("Invalid JWT");
            log.error("JWT is invalid.");
        }

        response.getWriter().write(objectMapper.writeValueAsString(exceptionBody));
    }
}