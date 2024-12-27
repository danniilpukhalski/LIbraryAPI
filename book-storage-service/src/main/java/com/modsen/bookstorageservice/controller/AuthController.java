package com.modsen.bookstorageservice.controller;


import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.dto.UserDto;
import com.modsen.bookstorageservice.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.dto.auth.JwtResponse;
import com.modsen.bookstorageservice.dto.auth.RefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth controller",description = "AuthAPI")
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "Login")
    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Register")
    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        return authService.register(userDto);

    }

    @Operation(summary = "Refresh")
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest refreshRequest) {

        return authService.refresh(refreshRequest.getToken());
    }
}