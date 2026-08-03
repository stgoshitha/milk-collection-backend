package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    // Find role by role name
    Optional<Role> findByRoleName(String roleName);

    // Check whether role already exists
    boolean existsByRoleName(String roleName);
}
