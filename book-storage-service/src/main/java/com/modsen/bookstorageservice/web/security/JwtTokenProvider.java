package com.modsen.bookstorageservice.web.security;

import com.modsen.bookstorageservice.domain.Role;
import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.service.props.JwtProperties;
import com.modsen.bookstorageservice.web.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class JwtTokenProvider {

    final JwtProperties jwtProperties;
    final UserDetailsService userDetailsService;
    final UserService userService;
    final UserMapper userMapper;
    SecretKey key;

    @PostConstruct
    public void init() {

        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);
        claimsBuilder.add("id", userId);
        claimsBuilder.add("roles", resolveRoles(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        claimsBuilder.expiration(validity);
        claimsBuilder.issuedAt(now);
        Claims claims = claimsBuilder.build();
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username) {
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);
        claimsBuilder.add("id", userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        claimsBuilder.expiration(validity);
        claimsBuilder.issuedAt(now);
        return Jwts.builder()
                .claims(claimsBuilder.build())
                .signWith(key)
                .compact();

    }

    @Transactional
    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied.");
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userMapper.toEntity(userService.getUserById(userId));
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));
        return jwtResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return !claims.getPayload().getExpiration().before(new Date());
    }

    private String getId(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    private String getUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }
}