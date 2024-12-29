package com.modsen.bookstorageservice.controller;


import com.modsen.bookstorageservice.dto.UserDto;
import com.modsen.bookstorageservice.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.dto.auth.RefreshRequest;
import com.modsen.bookstorageservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth controller", description = "AuthAPI")
@Slf4j
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "Login")
    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        log.info("Received login request. Username: {}", loginRequest.getUsername());
        JwtResponse response = authService.login(loginRequest);
        log.info("Successful login for user: {}", loginRequest.getUsername());
        return response;
    }

    @Operation(summary = "Register")
    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        log.info("Received registration request. Username: {}", userDto.getUsername());
        UserDto registeredUser = authService.register(userDto);
        log.info("Successful registration for user: {}", registeredUser.getUsername());
        return registeredUser;
    }

    @Operation(summary = "Refresh")
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest refreshRequest) {
        log.info("Received token refresh request.");
        JwtResponse response = authService.refresh(refreshRequest.getToken());
        log.info("Token successfully refreshed.");
        return response;
    }
}