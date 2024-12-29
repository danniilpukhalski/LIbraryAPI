package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.dto.UserDto;
import com.modsen.bookstorageservice.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.security.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserService userService;
    UserMapper userMapper;


    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        log.info("User login in progress: {}", loginRequest.getUsername());

        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        log.info("Authentication successful for user {}", loginRequest.getUsername());

        User user = userMapper.toEntity(userService.getUserByUsername(loginRequest.getUsername()));
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        log.info("JWT tokens generated for user: {}", user.getUsername());
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        log.info("Refreshing tokens using refresh token");

        JwtResponse jwtResponse = jwtTokenProvider.refreshUserTokens(refreshToken);

        log.info("Tokens refreshed successfully");
        return jwtResponse;
    }

    @Override
    public UserDto register(UserDto userDto) {
        log.info("Registering new user: {}", userDto.getUsername());

        UserDto registeredUser = userService.createUser(userDto);

        log.info("User registered: {}", registeredUser.getUsername());
        return registeredUser;
    }
}