package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.web.dto.UserDto;
import com.modsen.bookstorageservice.web.dto.auth.JwtRequest;
import com.modsen.bookstorageservice.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

    UserDto register(UserDto userDto);
}
