package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.web.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto getUserById(Long id);

    UserDto updateUser(UserDto userDto);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();

    UserDto getUserByUsername(String username);
}

