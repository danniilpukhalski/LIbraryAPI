package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.Role;
import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.dto.UserDto;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.repository.UserRepository;
import com.modsen.bookstorageservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDto getUserById(Long id) {
        log.info("Attempting to retrieve user with ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new ResourceNotFoundException("User with " + id + " not found");
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDto getUserByUsername(String username) {
        log.info("Attempting to retrieve user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User with username {} not found", username);
                    return new ResourceNotFoundException("User with username " + username + " not found");
                });
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Attempting to update user with ID: {}", userDto.getId());
        userRepository.findById(userDto.getId()).orElseThrow(() -> {
            log.error("User with ID {} not found", userDto.getId());
            return new ResourceNotFoundException("User with " + userDto.getId() + " not found");
        });

        if (userRepository.findByUsername(userDto.getUsername()).isPresent() &&
                !userRepository.findByUsername(userDto.getUsername()).get().getId().equals(userDto.getId())) {
            log.error("Username {} already exists", userDto.getUsername());
            throw new DuplicateResourceException("Username " + userDto.getUsername() + " already exists");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User with ID: {} was successfully updated", userDto.getId());

        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Attempting to create user with username: {}", userDto.getUsername());

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            log.error("Username {} already exists", userDto.getUsername());
            throw new IllegalStateException("Username " + userDto.getUsername() + " already exists");
        }

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            log.error("Passwords do not match for user {}", userDto.getUsername());
            throw new IllegalStateException("Passwords do not match");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);

        log.info("User with username: {} successfully created", userDto.getUsername());
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with ID {} not found", id);
            return new ResourceNotFoundException("User with " + id + " not found");
        });

        userRepository.delete(user);
        log.info("User with ID: {} successfully deleted", id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public List<UserDto> getAllUsers() {
        log.info("Retrieving list of all users");
        List<UserDto> users = userMapper.toDto(userRepository.findAll());
        log.info("Number of users: {}", users.size());
        return users;
    }
}
