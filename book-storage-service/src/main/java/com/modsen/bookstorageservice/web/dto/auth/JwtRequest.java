package com.modsen.bookstorageservice.web.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {

    @NotEmpty(message = "Username must be not null")
    String username;

    @NotEmpty(message = "Password must be not null")
    String password;
}
