package com.project.milkcollection.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;//Secret key used for signing JWT tokens
    private long expiration;//Access token expiration time in milliseconds
    private long refreshTokenExpiration;//Refresh token expiration time in milliseconds
}
