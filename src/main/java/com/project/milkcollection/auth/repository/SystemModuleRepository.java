package com.project.milkcollection.auth.repository;

import com.project.milkcollection.auth.entity.SystemModule;
import com.project.milkcollection.common.enums.CommonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemModuleRepository extends JpaRepository<SystemModule, UUID> {

    // Check whether system module name already exists
    boolean existsBySystemModuleName(String systemModuleName);

    // Find system module by display order
    Optional<SystemModule> findByDisplayOrder(Integer displayOrder);

    // Get the highest display order
    @Query("SELECT MAX(m.displayOrder) FROM SystemModule m")
    Optional<Integer> findMaxDisplayOrder();

    // Find system modules by status
    List<SystemModule> findByStatus(CommonStatus status);

    // Find active system modules ordered by display order
    List<SystemModule> findByStatusOrderByDisplayOrderAsc(CommonStatus status);
}