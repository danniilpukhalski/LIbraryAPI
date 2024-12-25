package com.modsen.bookstorageservice.service.props;

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
    long access;
    long refresh;

}
