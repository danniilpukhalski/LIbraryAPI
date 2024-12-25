package com.modsen.bookstorageservice.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    Long id;
    String username;
    String accessToken;
    String refreshToken;

}
