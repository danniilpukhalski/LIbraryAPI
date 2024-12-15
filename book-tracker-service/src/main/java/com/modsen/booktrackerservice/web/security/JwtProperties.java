package com.modsen.booktrackerservice.web.security;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

    private String secret = "amhnY3Ziamtqa2hnZmZjZnZibmJ2Y2dlcmhmamdoa3ZjZ2ZnaGosYnZjZmdqaGJ2Y2ZnaGI=";

}
