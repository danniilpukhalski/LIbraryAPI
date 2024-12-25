package com.modsen.bookstorageservice.web.security;

import com.modsen.bookstorageservice.domain.User;
import com.modsen.bookstorageservice.service.UserService;
import com.modsen.bookstorageservice.web.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUserDetailsService implements UserDetailsService {

    UserService userService;
    UserMapper userMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.toEntity(userService.getUserByUsername(username));
        return JwtEntityFactory.createJwtEntity(user);
    }
}

