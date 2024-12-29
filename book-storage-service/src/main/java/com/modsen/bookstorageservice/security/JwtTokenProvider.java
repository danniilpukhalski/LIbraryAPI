package com.modsen.bookstorageservice.security;

import com.modsen.bookstorageservice.domain.Role;
import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.service.props.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JwtTokenProvider {

    final JwtProperties jwtProperties;
    final UserDetailsService userDetailsService;
    final UserService userService;
    final UserMapper userMapper;
    SecretKey key;

    @PostConstruct
    public void init() {
        log.info("Initializing JWT key for signing and validation.");
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        log.info("JWT key initialized successfully.");
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        log.info("Creating access token for user: {} with ID: {}", username, userId);

        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);
        claimsBuilder.add("id", userId);
        claimsBuilder.add("roles", resolveRoles(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        claimsBuilder.expiration(validity);
        claimsBuilder.issuedAt(now);
        Claims claims = claimsBuilder.build();

        String token = Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();

        log.info("Access token successfully created for user: {}", username);
        return token;
    }

    private List<String> resolveRoles(Set<Role> roles) {
        log.debug("Resolving roles for the user.");
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username) {
        log.info("Creating refresh token for user: {} with ID: {}", username, userId);

        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);
        claimsBuilder.add("id", userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        claimsBuilder.expiration(validity);
        claimsBuilder.issuedAt(now);

        String token = Jwts.builder()
                .claims(claimsBuilder.build())
                .signWith(key)
                .compact();

        log.info("Refresh token successfully created for user: {}", username);
        return token;
    }

    @Transactional
    public JwtResponse refreshUserTokens(String refreshToken) {
        log.info("Refreshing tokens for refresh token: {}", refreshToken);

        JwtResponse jwtResponse = new JwtResponse();
        if (!validateToken(refreshToken)) {
            log.warn("Invalid or expired refresh token: {}", refreshToken);
            throw new AccessDeniedException("Access denied.");
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userMapper.toEntity(userService.getUserById(userId));

        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));

        log.info("Successfully refreshed tokens for user: {}", user.getUsername());
        return jwtResponse;
    }

    public boolean validateToken(String token) {
        log.debug("Validating token.");

        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            boolean isValid = !claims.getPayload().getExpiration().before(new Date());
            if (!isValid) {
                log.warn("Token is expired.");
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error occurred while validating token: {}", e.getMessage(), e);
            return false;
        }
    }

    private String getId(String token) {
        log.debug("Extracting user ID from token.");

        String userId = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();

        log.debug("User ID extracted from token: {}", userId);
        return userId;
    }

    private String getUsername(String token) {
        log.debug("Extracting username from token.");

        String username = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        log.debug("Username extracted from token: {}", username);
        return username;
    }

    public Authentication getAuthentication(String token) {
        log.info("Getting authentication for token: {}", token);

        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        log.info("Authentication successful for user: {}", username);
        return authentication;
    }
}