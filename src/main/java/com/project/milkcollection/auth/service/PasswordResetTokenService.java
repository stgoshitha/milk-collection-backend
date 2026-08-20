package com.project.milkcollection.auth.service;

import com.project.milkcollection.auth.entity.User;

public interface PasswordResetTokenService {

    void saveToken(User user, String rawToken);

    User validateToken(String rawToken);

    void markTokenAsUsed(String rawToken);

    void invalidateExistingTokens(User user);
}