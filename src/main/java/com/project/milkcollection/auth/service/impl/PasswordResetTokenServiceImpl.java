package com.project.milkcollection.auth.service.impl;

import com.project.milkcollection.auth.entity.PasswordResetToken;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.repository.PasswordResetTokenRepository;
import com.project.milkcollection.auth.service.PasswordResetTokenService;
import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.exception.UnauthorizedException;
import com.project.milkcollection.auth.service.PasswordResetTokenHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl
        implements PasswordResetTokenService {

    private static final long TOKEN_EXPIRATION_MINUTES = 15;

    private final PasswordResetTokenRepository
            passwordResetTokenRepository;

    private final PasswordResetTokenHashService
            passwordResetTokenHashService;

    @Override
    @Transactional
    public void saveToken(
            User user,
            String rawToken
    ) {

        /*
         * Remove old reset tokens belonging to
         * this user.
         */
        invalidateExistingTokens(user);

        String tokenHash =
                passwordResetTokenHashService.hash(
                        rawToken
                );

        PasswordResetToken passwordResetToken =
                new PasswordResetToken();

        passwordResetToken.setUser(user);

        passwordResetToken.setTokenHash(
                tokenHash
        );

        passwordResetToken.setExpiryDate(
                LocalDateTime.now()
                        .plusMinutes(
                                TOKEN_EXPIRATION_MINUTES
                        )
        );

        passwordResetToken.setCreatedAt(
                LocalDateTime.now()
        );

        passwordResetTokenRepository.save(
                passwordResetToken
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User validateToken(
            String rawToken
    ) {

        String tokenHash =
                passwordResetTokenHashService.hash(
                        rawToken
                );

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_RESET_TOKEN
                                )
                        );

        if (resetToken.getUsedAt() != null) {

            throw new UnauthorizedException(
                    ResponseMessage.INVALID_RESET_TOKEN
            );
        }

        if (resetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new UnauthorizedException(
                    ResponseMessage.RESET_TOKEN_EXPIRED
            );
        }

        return resetToken.getUser();
    }

    @Override
    @Transactional
    public void markTokenAsUsed(
            String rawToken
    ) {

        String tokenHash =
                passwordResetTokenHashService.hash(
                        rawToken
                );

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        ResponseMessage.INVALID_RESET_TOKEN
                                )
                        );

        resetToken.setUsedAt(
                LocalDateTime.now()
        );

        passwordResetTokenRepository.save(
                resetToken
        );
    }

    @Override
    @Transactional
    public void invalidateExistingTokens(
            User user
    ) {

        passwordResetTokenRepository
                .deleteAllByUser_UserId(
                        user.getUserId()
                );
    }
}