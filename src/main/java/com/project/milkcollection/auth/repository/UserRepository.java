package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {
            "role",
            "role.rolePermissions",
            "role.rolePermissions.permission"
    })
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {
            "role",
            "role.rolePermissions",
            "role.rolePermissions.permission"
    })
    Optional<User> findByUsernameOrEmail(
            String username,
            String email
    );

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRole_RoleName(String roleName);

    boolean existsByUsernameAndUserIdNot(
            String username,
            UUID userId
    );

    boolean existsByEmailAndUserIdNot(
            String email,
            UUID userId
    );

}