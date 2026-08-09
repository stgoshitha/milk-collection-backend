package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    // Find all permissions assigned to a role
    List<RolePermission> findByRoleRoleId(UUID roleId);

    // Check whether a permission is already assigned to a role
    boolean existsByRoleRoleIdAndPermissionPermissionId(
            UUID roleId,
            UUID permissionId
    );

    // Delete all permission assignments for a role
    void deleteByRoleRoleId(UUID roleId);
}
