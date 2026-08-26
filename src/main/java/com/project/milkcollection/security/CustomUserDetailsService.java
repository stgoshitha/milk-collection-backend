package com.project.milkcollection.security;

import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.repository.UserRepository;
import com.project.milkcollection.common.constants.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Service responsible for loading user details during authentication.
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Load user by username for Spring Security authentication.
    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                ResponseMessage.INVALID_CREDENTIALS
                        )
                );

        return new CustomUserDetails(user);
    }
}