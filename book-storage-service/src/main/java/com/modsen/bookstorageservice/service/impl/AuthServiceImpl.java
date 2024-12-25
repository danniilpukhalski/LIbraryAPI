package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.web.dto.UserDto;
import com.modsen.bookstorageservice.web.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.web.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import com.modsen.bookstorageservice.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserService userService;
    UserMapper userMapper;


    @Override
    public JwtResponse login(JwtRequest loginRequest) {

        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userMapper.toEntity(userService.getUserByUsername(loginRequest.getUsername()));
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {

        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

    @Override
    public UserDto register(UserDto userDto) {
        return userService.createUser(userDto);
    }
}