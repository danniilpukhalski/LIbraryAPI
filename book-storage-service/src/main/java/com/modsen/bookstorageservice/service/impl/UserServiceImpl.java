package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.Role;
import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.repository.UserRepository;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.web.dto.UserDto;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDto getUserById(Long id) {

        return userMapper.toDto(userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found")));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDto getUserByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found")));

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        userRepository.findById(userDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()&&
                !userRepository.findByUsername(userDto.getUsername()).get().getId().equals(userDto.getId())) {
            throw new DuplicateResourceException("Username already exists");
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new IllegalStateException("Passwords do not match");
        }
        User user = userMapper.toEntity(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDeleted(false);
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
        user.setDeleted(true);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userRepository.findAll());
    }
}
