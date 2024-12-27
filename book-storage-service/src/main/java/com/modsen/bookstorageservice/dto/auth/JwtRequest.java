package com.modsen.bookstorageservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "username", example = "user")
    String username;

    @NotEmpty(message = "Password must be not null")
    @Schema(description = "password", example = "user")
    String password;
}
