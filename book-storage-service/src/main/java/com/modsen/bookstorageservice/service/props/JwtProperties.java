package com.modsen.bookstorageservice.service.props;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    private String secret = "amhnY3Ziamtqa2hnZmZjZnZibmJ2Y2dlcmhmamdoa3ZjZ2ZnaGosYnZjZmdqaGJ2Y2ZnaGI=";
    private long access = 3600000;
    private long refresh = 259200000;

}
