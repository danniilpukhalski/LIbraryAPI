package com.modsen.bookstorageservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class RefreshRequest {

    @NotEmpty
    @Schema(description = "refresh token")
    private String token;
}
