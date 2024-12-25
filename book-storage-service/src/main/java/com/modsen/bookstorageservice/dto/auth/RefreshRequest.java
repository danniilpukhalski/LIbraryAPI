package com.modsen.bookstorageservice.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class RefreshRequest {
    @NotEmpty
    private String token;
}
