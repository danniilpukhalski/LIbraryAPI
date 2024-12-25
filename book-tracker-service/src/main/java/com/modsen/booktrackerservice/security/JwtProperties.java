package com.modsen.booktrackerservice.security;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "credentials")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperties {

    String secret;

}
