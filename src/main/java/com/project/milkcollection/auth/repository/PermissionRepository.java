package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.Permission;
import com.project.milkcollection.common.enums.CommonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    // Check whether permission name already exists
    boolean existsByPermissionName(String permissionName);

    // Find all permissions belonging to a system module
    List<Permission> findBySystemModuleSystemModuleId(UUID moduleId);

}
