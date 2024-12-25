package com.modsen.bookstorageservice.web.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtEntity implements UserDetails {

    Long id;
    String username;
    String password;
    Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {

        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {

        return UserDetails.super.isEnabled();
    }
}