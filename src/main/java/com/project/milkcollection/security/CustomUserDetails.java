package com.project.milkcollection.security;

import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.common.constants.SecurityConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Custom implementation of Spring Security UserDetails for authentication
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // Logged-in user entity information
    private final User user;

    // Convert user role into Spring Security authorities
    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities(){

        String rolePrefix = SecurityConstants.ROLE_PREFIX;

        return List.of(
                new SimpleGrantedAuthority(
                        rolePrefix + user.getRole().getRoleName()
                )
        );
    }

    // Return encrypted password for authentication verification
    @Override
    @NonNull
    public String getPassword(){
        return user.getPassword();
    }

    // Return username used for login identification
    @Override
    @NonNull
    public String getUsername(){
        return user.getUsername();
    }

    // Check whether user account is expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Check whether user account is locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Check whether user credentials are expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Enable authentication only for active users
    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

}