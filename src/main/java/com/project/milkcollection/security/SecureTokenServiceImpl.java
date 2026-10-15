package com.project.milkcollection.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SecureTokenServiceImpl implements SecureTokenService {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateToken() {

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}