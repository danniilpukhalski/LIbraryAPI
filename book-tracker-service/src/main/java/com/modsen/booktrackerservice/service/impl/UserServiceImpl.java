package com.modsen.booktrackerservice.service.impl;

import com.modsen.booktrackerservice.domain.User;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.repository.UserRepository;
import com.modsen.booktrackerservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public User getUserByUsername(String username) {
        log.info("Attempting to retrieve user with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username: {} not found", username);
                    return new ResourceNotFoundException("User with " + username + " not found");
                });
    }
}
