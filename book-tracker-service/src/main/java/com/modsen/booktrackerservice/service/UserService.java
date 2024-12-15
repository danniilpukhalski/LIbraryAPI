package com.modsen.booktrackerservice.service;

import com.modsen.booktrackerservice.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserByUsername(String username);
}
