package com.project.milkcollection.security;

import com.project.milkcollection.common.constants.ResponseMessage;
import com.project.milkcollection.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static UUID getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof CustomUserDetails userDetails)) {

            throw new UnauthorizedException(
                    ResponseMessage.UNAUTHORIZED
            );
        }

        return userDetails.getUserId();
    }
}