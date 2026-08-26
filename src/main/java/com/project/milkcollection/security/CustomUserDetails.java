package com.project.milkcollection.security;

import com.project.milkcollection.auth.entity.Permission;
import com.project.milkcollection.auth.entity.RolePermission;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.common.constants.SecurityConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

// Custom implementation of Spring Security UserDetails for authentication
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // Logged-in user entity information
    private final User user;

    public UUID getUserId(){
        return user.getUserId();
    }

    // Convert user role into Spring Security authorities
    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        /*
         * Add role authority.
         *
         * Example:
         * ADMIN -> ROLE_ADMIN
         * COLLECTOR -> ROLE_COLLECTOR
         */
        String roleAuthority =
                SecurityConstants.ROLE_PREFIX
                        + user.getRole().getRoleName();

        authorities.add(
                new SimpleGrantedAuthority(roleAuthority)
        );

        /*
         * Add permission authorities.
         *
         * Example:
         * USER_VIEW
         * USER_CREATE
         * FARMER_VIEW
         * FARMER_CREATE
         */
        for (RolePermission rolePermission :
                user.getRole().getRolePermissions()) {

            Permission permission = rolePermission.getPermission();

            authorities.add(
                    new SimpleGrantedAuthority(
                            permission.getPermissionName()
                    )
            );
        }

        return authorities;
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