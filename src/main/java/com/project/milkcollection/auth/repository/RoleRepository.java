package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    // Find a role by its display name.
    Optional<Role> findByRoleName(String roleName);

    // Find a role by its unique system code.
    Optional<Role> findByRoleCode(String roleCode);

    // Check whether a role code already exists.
    boolean existsByRoleCode(String roleCode);

    // Check whether a role name already exists.
    boolean existsByRoleName(String roleName);

    // Check whether another role already uses the given role code.
    boolean existsByRoleCodeIgnoreCaseAndRoleIdNot(String roleCode, UUID roleId);

    // Check whether another role already uses the given role name.
    boolean existsByRoleNameIgnoreCaseAndRoleIdNot(String roleName, UUID roleId);

}