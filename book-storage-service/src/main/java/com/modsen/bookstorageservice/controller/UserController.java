package com.modsen.bookstorageservice.controller;

import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.dto.UserDto;
import com.modsen.bookstorageservice.dto.validation.OnCreate;
import com.modsen.bookstorageservice.dto.validation.OnUpdate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Transactional
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {

        return userService.updateUser(userDto);
    }

    @Transactional
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {

        return userService.getAllUsers();
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto createUser(@Validated(OnCreate.class)@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

}
