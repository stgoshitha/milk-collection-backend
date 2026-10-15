package com.project.milkcollection.config.initializer;

import com.project.milkcollection.auth.entity.Role;
import com.project.milkcollection.auth.entity.User;
import com.project.milkcollection.auth.entity.enums.UserStatus;
import com.project.milkcollection.auth.repository.RoleRepository;
import com.project.milkcollection.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial-admin.enabled:false}")
    private boolean initialAdminEnabled;

    @Value("${app.initial-admin.username:admin}")
    private String initialAdminUsername;

    @Value("${app.initial-admin.email:admin@example.com}")
    private String initialAdminEmail;

    @Value("${app.initial-admin.full-name:System Administrator}")
    private String initialAdminFullName;

    @Value("${app.initial-admin.password:}")
    private String initialAdminPassword;

    @Override
    @Transactional
    public void run(String... args) {

        if (!initialAdminEnabled) {
            log.debug("Initial admin creation is disabled.");
            return;
        }

        if (userRepository.existsByRole_RoleName("ADMIN")) {
            log.debug("ADMIN user already exists. Skipping initialization.");
            return;
        }

        validateInitialAdminPassword();

        Role adminRole = roleRepository
                .findByRoleName("ADMIN")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "ADMIN role must exist before creating the initial admin user"
                        )
                );

        validateUsernameAndEmail();

        User admin = User.builder()
                .fullName(initialAdminFullName)
                .username(initialAdminUsername)
                .email(initialAdminEmail)
                .password(passwordEncoder.encode(initialAdminPassword))
                .role(adminRole)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);

        log.info(
                "Initial ADMIN user created successfully with username '{}'.",
                initialAdminUsername
        );
    }

    private void validateInitialAdminPassword() {

        if (initialAdminPassword == null ||
                initialAdminPassword.isBlank()) {

            throw new IllegalStateException(
                    "Initial admin password must be configured"
            );
        }
    }

    private void validateUsernameAndEmail() {

        if (userRepository.existsByUsername(initialAdminUsername)) {
            throw new IllegalStateException(
                    "Cannot create initial ADMIN: username already exists"
            );
        }

        if (userRepository.existsByEmail(initialAdminEmail)) {
            throw new IllegalStateException(
                    "Cannot create initial ADMIN: email already exists"
            );
        }
    }
}