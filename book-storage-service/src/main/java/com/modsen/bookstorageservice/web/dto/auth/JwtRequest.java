package com.modsen.bookstorageservice.web.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @NotEmpty(message = "Username must be not null")
    private String username;

    @NotEmpty(message = "Password must be not null")
    private String password;
}
